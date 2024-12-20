package ptp.fltv.web.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.transfer.TransferManager;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pfp.fltv.common.constants.OAuth2LoginConstants;
import pfp.fltv.common.constants.RedisConstants;
import pfp.fltv.common.constants.WebConstants;
import pfp.fltv.common.enums.Gender;
import pfp.fltv.common.enums.LoginClientType;
import pfp.fltv.common.exceptions.PtpException;
import pfp.fltv.common.model.po.info.AddressInfo;
import pfp.fltv.common.model.po.info.LoginInfo;
import pfp.fltv.common.model.po.manage.*;
import pfp.fltv.common.model.vo.UserLoginVo;
import pfp.fltv.common.model.vo.UserVo;
import pfp.fltv.common.utils.JwtUtils;
import ptp.fltv.web.constants.CosConstants;
import ptp.fltv.web.mapper.UserMapper;
import ptp.fltv.web.service.*;
import ptp.fltv.web.utils.RandomUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.args.GeoUnit;
import redis.clients.jedis.params.GeoSearchParam;
import redis.clients.jedis.resps.GeoRadiusResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 7:12:27
 * @description 用户服务接口的实现类
 * @filename UserServiceImpl.java
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    private final StringRedisTemplate redisTemplate;
    private final RoleService roleService;
    private final Jedis jedis;
    private final TransferManager transferManager;
    private final COSClient cosClient;
    private final SubscriberShipService subscriberShipService;
    private final PermissionService permissionService;


    @Override
    public User getUserByNickname(@NonNull String nickname) {

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickname, nickname);

        return baseMapper.selectOne(queryWrapper);

    }


    @Override
    public Map<String, Object> loginByNicknameAndPassword(@Nonnull UserLoginVo userLoginVo) throws PtpException {

        User user = getUserByNickname(userLoginVo.getNickname());

        if (user == null) {

            throw new PtpException(802);

            // 2024-5-3  22:24-TODO 同样的，也需要专门的密码加密器(这里暂时先进行直接比较)
        } else if (!userLoginVo.getPassword().equals(user.getPassword())/*!Objects.equals(passwordEncoder.encode(userLoginVo.getPassword()), user.getPassword())*/) {

            throw new PtpException(803);

        }

        // 2024-6-17  22:08-后端无需考虑设备码的具体实现，该实现将交由各端开发人员负责实现，这里只需要知道deviceID为 {WEB/MOBILE/PC}:设备码 即可
        String deviceID = userLoginVo.getLoginInfo().getDeviceInfo().getDeviceID();

        LoginClientType loginClientType = LoginClientType.valueOf(deviceID.split(":")[0]);
        String deviceId = deviceID.split(":")[1];


        // 2024-4-4  21:18 保存用户敏感信息和权限认证信息到分布式Redis中，同时返回对应的SessionId

        Role role = roleService.getRoleByUser(user);

        // 2024-12-6  16:11-由于采用RBAC权限模型后 , 由于权限不再和角色一起存储而是分开存放 , 因此仅仅获取用户角色信息还是不够 , 还需要继续凭借用户的角色信息去获取对应的权限信息才可以
        List<String> permissionExpressions = permissionService.findPermissionsByRoleId(role.getId())
                .stream()
                .map(Permission::getExpression)
                .toList();

        Role compactRole = Role.builder()
                .id(role.getId())
                .name(role.getName())
                .authorities(permissionExpressions)
                // .authorities(role.getAuthorities())
                // .prohibition(role.getProhibition())
                .build();

        // 2024-4-4  21:10-为避免user数据过大，将适当地对一些非必要字段进行赋空值操作
        User compactUser = User.builder()
                .id(user.getId())
                .account(user.getAccount())
                .password(user.getPassword())
                .status(user.getStatus())
                .roleId(user.getRoleId())
                .build();

        // 2024-4-5  22:17-先不要用ID+""的形式加密，这样做解密的时候会被转换为"ID"
        // TODO 目前先用用户ID作为key，日后将单独与远程随机ID生成服务进行交互获取
        Long STORE_KEY = user.getId();

        // 2024-4-7  9:39-缓存的超时时间暂定为24H
        redisTemplate.opsForValue().set("user:login:" + STORE_KEY, JSON.toJSONString(compactUser), RedisConstants.CACHE_TIMEOUT, TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set("user:role:" + STORE_KEY, JSON.toJSONString(compactRole), RedisConstants.CACHE_TIMEOUT, TimeUnit.MILLISECONDS);

        // 2024-6-17  22:23-存储三端各自的登录环境信息
        Map<String, Object> env = new HashMap<>();
        env.put("client-type", loginClientType);
        env.put("device-id", deviceId);
        env.put("login-datetime", LocalDateTime.now());
        env.put("login-info-details", userLoginVo.getLoginInfo());

        Map<String, Object> result = new HashMap<>();
        result.put("isLoginSafely", true); // 2024-11-8  22:31-默认登录环境是安全的(不过真正安不安全需要看后面的异地登录校验的结果)

        String envKey = String.format("user:login:env:%s:%d", loginClientType.name().toLowerCase(), STORE_KEY);

        String oldLoginEnvStr = redisTemplate.opsForValue().get(envKey);
        if (StringUtils.hasLength(oldLoginEnvStr)) {

            JSONObject oldLoginEnvJsonObj = JSON.parseObject(oldLoginEnvStr);

            checkLoginSecurity(env, oldLoginEnvJsonObj, result); // 2024-11-8  21:32-检查当前账号是否存在异地登录的情况 , 如果存在 , 还需要将异地登录的证据存入响应结果中

        }

        redisTemplate.opsForValue().set(envKey, JSON.toJSONString(env), RedisConstants.CACHE_TIMEOUT, TimeUnit.MILLISECONDS);

        userLoginVo.setPassword(""); // 2024-4-3  20:51-清除用户敏感信息
        String jwt1 = JwtUtils.encode(userLoginVo);
        result.put(WebConstants.USER_LOGIN_COOKIE_KEY, jwt1);
        String jwt2 = JwtUtils.encode(STORE_KEY);
        result.put("store_key", jwt2);
        // 2024-8-7  16:38-信息不需要脱敏,一方面是因为密码信息本身就是单向加密过的 , 另一方面则是因为该user数据仅流通于应用系统内部 , 并不会向用户/外界暴露
        // 2024-9-28  20:59-解决客户端无法反序列化User对象的问题 , 请注意 , 后端返回对象实体时必须将其以JSON字符串的形式进行填充后返回
        result.put("user", JSON.toJSONString(user));

        // 2024-6-17  22:32-登录客户端类型和设备码无需返回给前端，因为这些数据完全可以再次由前端计算出来，而且计算是幂等的，至于每一次请求是否需要重新计算一遍，这得看前端那边怎么处理了，与我们后端无关~
        // 2024-4-3  21:33-返回给前端，保存到LocalStorage那里，用的时候再让前端带过来
        return result;

    }


    @Override
    public Map<String, Object> loginBy3rdUid(UserLoginVo userLoginVo) {

        // 2024-11-25  15:8-先根据用户方提供的OpenId去查询是否存在关联的用户 , 如果存在则执行正常的登录流程 , 否则 , 则拒绝登录
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        switch (userLoginVo.getLoginType()) {

            case QQ -> queryWrapper.eq("qq_account", userLoginVo.getAccount());
            case WECHAT -> queryWrapper.eq("wechat_account", userLoginVo.getAccount());
            case WEIBO -> queryWrapper.eq("weibo_account", userLoginVo.getAccount());
            default -> throw new PtpException(808, "未知的第三方登录方式!");

        }


        List<User> users = list(queryWrapper);
        if (users.isEmpty()) {

            // 2024-11-25  15:50-如果查询不到关联的用户 , 则说明当前QQ尚未绑定其他现有的PTP账号 , 因此拒绝本次用户登录
            throw new PtpException(808, "当前QQ尚未关联任何PTP账号!");

        } else {

            // 2024-11-25  15:56-如果存在 , 则选中第一个候选用户进行登录(按理说 , 一个QQ和一个PTP账号是1:1关联的关系才对)
            User candidateUser = users.get(0);

            // 2024-11-25  15:57-重新装配userLoginInfo进行正常化登录(这里为了方便直接调用现有的普通登录API)
            userLoginVo.setNickname(candidateUser.getNickname());
            userLoginVo.setPassword(candidateUser.getPassword());
            userLoginVo.setAccount(null);

            return loginByNicknameAndPassword(userLoginVo);

        }

    }


    /**
     * @param newEnvMap     当前正在进行登录的登录环境
     * @param oldEnvJsonObj 云端存储的上一次登录的环境信息
     * @param result        即将要返回给用户的登录结果
     * @author Lenovo/LiGuanda
     * @date 2024/11/8 PM 9:40:54
     * @version 1.0.0
     * @description 查看当前账号是否存在异地登录的异常行为
     * @filename UserServiceImpl.java
     */
    private void checkLoginSecurity(Map<String, Object> newEnvMap, JSONObject oldEnvJsonObj, Map<String, Object> result) {

        try {

            result.put("isLoginSafely", true);

            if (newEnvMap != null && oldEnvJsonObj != null) {

                // 2024-11-8  22:36-先按照登录的设备ID是否一致判断一下登录环境的安全性
                if (newEnvMap.containsKey("device-id") && oldEnvJsonObj.containsKey("device-id") && !newEnvMap.get("device-id").equals(oldEnvJsonObj.getString("device-id"))) {

                    result.put("isLoginSafely", false);
                    result.put("oldEnvInfo", oldEnvJsonObj.toJSONString());

                }

                if (newEnvMap.containsKey("login-info-details") && oldEnvJsonObj.containsKey("login-info-details")) {

                    LoginInfo newLoginInfo = (LoginInfo) newEnvMap.get("login-info-details");
                    LoginInfo oldLoginInfo = JSON.parseObject(oldEnvJsonObj.get("login-info-details").toString(), LoginInfo.class);

                    if (newLoginInfo != null && oldLoginInfo != null) {

                        AddressInfo newAddressInfo = newLoginInfo.getAddressInfo();
                        AddressInfo oldAddressInfo = oldLoginInfo.getAddressInfo();
                        if (newAddressInfo != null && oldAddressInfo != null) {

                            if (!newAddressInfo.getCityID().equals(oldAddressInfo.getCityID())) { // 2024-11-8  22:35-再按照城市ID是否一致判断登录安全性

                                result.put("isLoginSafely", false);
                                result.put("oldEnvInfo", oldEnvJsonObj.toJSONString());

                            }

                        }

                    }

                }

            }

        } catch (Exception ex) {

            // 2024-11-8  22:07-这里即使在解析数据包的过程中出现异常也不应阻断后续的代码的进行
            log.error("[{}] : {} occurred : {}", "ptp-web-web : UserServiceImpl", ex.getClass().getName(), ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());

        }

    }


    @Override
    public String loginByGithub(@Nonnull String code) {

        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", OAuth2LoginConstants.GITHUB_CLIENT_ID);
        params.put("client_secret", OAuth2LoginConstants.GITHUB_CLIENT_SECRET);

        String credential = HttpUtil.createPost(OAuth2LoginConstants.GITHUB_ACCESS_TOKEN_URL)
                .header("Accept", "application/json")
                .form(params)
                .execute()
                .body();

        String token = JSON.parseObject(credential).getString("access_token");

        return HttpUtil.createGet(OAuth2LoginConstants.GITHUB_ACCESS_USER_INFO_URL)
                .header("Accept", "application/json")
                .header("Authorization", "token " + token)
                .execute()
                .body();

    }


    @Override
    public User refreshGeolocation(@Nonnull Long id, @Nonnull AddressInfo addressInfo) {

        Double longitude = addressInfo.getLongitude();
        Double latitude = addressInfo.getLatitude();

        if (longitude == null || latitude == null) {

            throw new PtpException(814, "用经度或纬度信息缺失", String.format("longitude = %s , latitude = %s", longitude, latitude));

        }

        final String key = "user:geolocation:current";

        // 2024-6-22  20:19-由于考虑到 附近的人 功能需要拉取用户数据 , 可是这个过程可能会因拉取大量用户数据而出现较长时间的卡顿 , 因此 , 我们决定将部分必要的用户数据直接存入Redis中 , 并且作为当前key的成员
        User user = baseMapper.selectById(id);
        if (user != null) {

            // 2024-6-22  20:25-移除掉 附近的人 社交功能用不到的非必要信息(FastJson2 默认不会序列化字段值为null的字段 , 这样就能在一定程度上降低单个成员的体积了)
            user.setPassword(null);
            user.setPhone(null);
            user.setEmail(null);
            user.setRealname(null);
            user.setIdiograph(null);
            user.setBackground(null);
            user.setLikeNum(null);
            user.setBirthDate(null);
            user.setAddress(null);
            user.setBindAccounts(null);
            user.setRoleId(null);
            user.setAssetId(null);
            user.setCreateTime(null);
            user.setUpdateTime(null);
            user.setIsDeleted(null);
            user.setVersion(null);


            // 2024-6-21  22:43-Redis GEO数据类型说明
            // Redis v3.2+ 新增了GEO数据类型 , 主要用于存储和操作地理位置信息
            // 其底层实现结构为 Sorted Set , 与 zset数据类型的底层实现结构相类似
            // 由于数据的分布是以二维的形式进行呈现的 , 而保存的时候要以一维的方式进行存储 , 因此需要将 经度&纬度 这两个度量指标进行合并映射
            // func(longitude , latitude) => geo_hash as score(zset)
            Long addedItems = redisTemplate.opsForGeo().add(key, new Point(longitude, latitude), JSON.toJSONString(user, JSONWriter.Feature.NotWriteDefaultValue, JSONWriter.Feature.NotWriteEmptyArray));

        }

        return user;

    }


    @Override
    public Map<Integer, List<User>> findPeopleNearby(@Nonnull Long id, @Nonnull Double longitude, @Nonnull Double latitude, @Nonnull Double radius, @Nonnull Long limit) {

        final String KEY = "user:geolocation:current";

        // final Map<Long, Double> id2distanceMap = new HashMap<>();
        final Map<Integer, List<User>> distance2UserMap = new HashMap<>();

        AddressInfo addressInfo = new AddressInfo();
        addressInfo.setLongitude(longitude);
        addressInfo.setLatitude(latitude);

        User currentRefreshedUser = refreshGeolocation(id, addressInfo);

        if (currentRefreshedUser == null) {

            throw new PtpException(811, "无法更新当前用户的位置信息", "执行 UserServiceImpl::refreshGeolocation 方法后返回的user为null");

        }

        // 2024-6-23  21:51-太好了 , Jedis的 geosearch 方法不仅会返回附近的人的坐标 , 还连距离一同计算并返回回来了,真是太省事了!!!并且相应速度平均是之前Redis客户端操作的1/3！！！
        GeoSearchParam geoSearchParam = new GeoSearchParam()
                .fromLonLat(longitude, latitude)
                .byRadius(radius, GeoUnit.KM)
                .count(Math.toIntExact(limit))
                .asc()
                .withCoord()
                .withDist()
                .withHash();
        List<GeoRadiusResponse> peoplesNearby = jedis.geosearch(KEY, geoSearchParam);

        if (peoplesNearby == null || peoplesNearby.isEmpty()) {

            return Collections.emptyMap();

        }

        // 2024-6-23  21:14-由于Redis通过Pipeline进行map操作比较不方便 , 因此改用jedis客户端进行pipeline下的map操作
        // 2024-6-23  10:04-但是由于我们的操作并不是特别耗时并且对于数据一致性可靠性要求不是特别高 , 因此这里不再走事务进行处理了
        // 2024-6-23  00:08-由于Redis的Pipeline并不能保证输入到Redis服务端的这一串命令原子化执行 , 因此我们需要使用Redis的事务来完成 附近的人 距离计算操作
        for (GeoRadiusResponse response : peoplesNearby) {

            User peopleNearby = JSON.parseObject(response.getMemberByString(), User.class);

            if (peopleNearby != null) {

                peopleNearby.setPassword(null);
                peopleNearby.setPhone(null);
                peopleNearby.setEmail(null);
                peopleNearby.setRealname(null);
                peopleNearby.setIdiograph(null);
                peopleNearby.setBackground(null);
                peopleNearby.setLikeNum(null);
                peopleNearby.setBirthDate(null);
                peopleNearby.setAddress(null);
                peopleNearby.setBindAccounts(null);
                peopleNearby.setRoleId(null);
                peopleNearby.setAssetId(null);
                peopleNearby.setCreateTime(null);
                peopleNearby.setUpdateTime(null);
                peopleNearby.setIsDeleted(null);
                peopleNearby.setVersion(null);

                // 2024-6-22  23:22-不要为了节约内存空间而把extension单独抽离到 forEach 外面!!!这样会使得全部的User最终都引用到了同一个最后一个User更新后的meta映射!!!
                Map<String, Object> extension = new HashMap<>();
                extension.put("distance", response.getDistance());
                extension.put("longitude", response.getCoordinate().getLongitude());
                extension.put("latitude", response.getCoordinate().getLatitude());
                extension.put("geo-hash", response.getRawScore());

                peopleNearby.setMeta(extension);

                int estimatedDistance = ((int) response.getDistance() / 10) * 10;

                if (!distance2UserMap.containsKey(estimatedDistance)) {

                    distance2UserMap.put(estimatedDistance, new ArrayList<>());

                }
                distance2UserMap.get(estimatedDistance).add(peopleNearby);

            }

        }

        return distance2UserMap;

    }


    @Override
    public void logout(@Nonnull LoginClientType clientType, @Nonnull Long userId, @Nonnull HttpServletResponse response) {

        // 2024-8-7  15:11-移除用户在云端Redis的登录数据信息
        redisTemplate.delete(String.format("user:login:%d", userId));
        redisTemplate.delete(String.format("user:role:%d", userId));
        redisTemplate.delete(String.format("user:login:env:%s:%d", clientType.name().toLowerCase(), userId));

        // 2024-8-7  15:23-更新对应的用户地理位置信息为0(或者可近似认为是无效值)
        AddressInfo addressInfo = AddressInfo.builder()
                .longitude(-1D)
                .latitude(-1D)
                .altitude(Double.MAX_VALUE)
                .detailedLocation("Invalid User Location Information")
                .build();
        refreshGeolocation(userId, addressInfo);

        // 2024-4-5  21:21-这边登出的时候，前端那边也要同步清除用户SESSION TOKEN信息，不需要清除登录信息
        // SecurityContextHolder.clearContext();

        // 2024-8-4  23:32-移除掉客户端浏览器的登录环境信息Cookie
        Cookie cookie = new Cookie(WebConstants.USER_LOGIN_COOKIE_KEY, null);
        cookie.setSecure(false);
        cookie.setHttpOnly(false);
        cookie.setMaxAge(0);
        cookie.setDomain(ptp.fltv.web.constants.WebConstants.SERVER_IP);
        cookie.setPath("/");
        response.addCookie(cookie);

    }


    @Override
    public String updateAvatar(@Nonnull Long userId, @Nonnull MultipartFile newAvatarFile) throws IOException, InterruptedException {

        String key = "avatar/" + userId + ".png";

        PutObjectRequest request = new PutObjectRequest(CosConstants.BUCKET_USER_PICTURE, key, newAvatarFile.getInputStream(), null);

        UploadResult uploadResult = transferManager.upload(request).waitForUploadResult();

        // 2024-8-11  20:13-八嘎 , 原来自己拼接的和getObjectUrl获取到的URL是一样的!!!
        // 2024-8-11  20:04-由于存储桶我们设置了私有读写的权限 , 因此拼接得到的URI以及getObjectUrl均不能直接访问目标资源 , 只能通过客户端凭借getObjectUrl得到的URI去访问云端资源文件
        String newAvatarUri = String.format(CosConstants.PUBLIC_REQUEST_URL_PREFIX, CosConstants.BUCKET_USER_PICTURE, "/" + key);

        User user = getById(userId);

        String oldAvatarUri = null;

        try {

            // 2024-8-12  17:58-旧头像URI的提取页同样需要进行异常处理 , 删除旧头像失败同样记录日志并交给人工处理
            JSONObject avatarJSONObject = JSON.parseObject(user.getAvatar());
            oldAvatarUri = avatarJSONObject.getString("uri");

        } catch (Exception e) {

            log.error("提取用户旧的头像URI出错", e);

        }

        Map<String, String> avatarMap = new HashMap<>();
        avatarMap.put("type", "uri");
        avatarMap.put("uri", newAvatarUri);

        user.setAvatar(JSON.toJSONString(avatarMap));

        // 2024-8-12  17:53-之所以我们在这里没有进行异常处理 , 是因为我们认为只要本次图片上传成功 , 但用户数据更新失败 , 则此次头像修改操作就认定为失败
        // 至于多上传的头像图片文件 , 同样交由人工进行补偿处理
        // 2024-8-12  17:52-服务端这里主动发起用户更新
        updateById(user);

        // 2024-8-11  19:26-能进行到这里 , 说明刚刚的新头像上传已经成功了 , 那现在就需要删除旧的图片文件(不开启文件版本的情况下)
        // 此时即使删除失败了 , 流程也要正常进行下去 , 因为此时删除失败不会对用户更新头像操作产生副作用了 , 只不过是服务器这边的资源被多占用了些而已
        // 后续可通过日志分析或者人工介入等操作来处理此类异常情况
        try {

            String uriPrefix = String.format(CosConstants.PUBLIC_REQUEST_URL_PREFIX, CosConstants.BUCKET_USER_PICTURE, "/");
            String oldAvatarKey = Objects.requireNonNull(oldAvatarUri).replace(uriPrefix, "");

            // 2024-8-12  22:19-如果前后头像URI一致 , 则说明本次更新为覆盖更新 , 即旧的头像存储策略与现在的一致 , 因此无需再额外删除(因为旧的头像文件已经被现在的新的头像文件给覆盖掉了); 否则 , 则去删除旧的头像资源文件
            if (!oldAvatarUri.equals(newAvatarUri)) {

                cosClient.deleteObject(CosConstants.BUCKET_USER_PICTURE, oldAvatarKey);

            }

        } catch (Exception e) {

            log.error("删除用户旧的头像资源文件出错", e);

        }

        return newAvatarUri;

    }


    @Override
    public List<User> queryFollowerPage(@Nonnull Long userId, @Nonnull Long pageNum, @Nonnull Long pageSize) {

        QueryWrapper<SubscriberShip> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("followee_id", userId);

        List<SubscriberShip> records = subscriberShipService.page(new Page<>(pageNum, pageSize), queryWrapper).getRecords();
        if (records != null && !records.isEmpty()) {

            List<Long> followerIds = records.stream()
                    .map(SubscriberShip::getFollowerId)
                    .toList();

            List<User> users = listByIds(followerIds);
            if (users != null && !users.isEmpty()) {

                return users;

            }

        }

        return new ArrayList<>();

    }


    @Override
    public List<UserVo> queryFollowerVoPage(@Nonnull Long userId, @Nonnull Long pageNum, @Nonnull Long pageSize) {

        QueryWrapper<SubscriberShip> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("followee_id", userId);

        List<SubscriberShip> records = subscriberShipService.page(new Page<>(pageNum, pageSize), queryWrapper).getRecords();
        if (records != null && !records.isEmpty()) {

            List<Long> followerIds = records.stream()
                    .map(SubscriberShip::getFollowerId)
                    .toList();

            List<User> users = listByIds(followerIds);
            if (users != null && !users.isEmpty()) {

                List<UserVo> userVos = new ArrayList<>();
                for (User user : users) {

                    UserVo userVo = new UserVo();
                    BeanUtils.copyProperties(user, userVo);

                    // 2024-11-4  16:37-单独查询当前订阅发起者与当前遍历的用户之间是否存在订阅关系(即当前请求用户是否订阅了当前遍历到的用户)
                    SubscriberShip subscriberShip = subscriberShipService.querySingleSubscription(userId, user.getId());
                    userVo.setIsSubscribed(subscriberShip != null);

                    userVos.add(userVo);

                }

                return userVos;

            }

        }

        return new ArrayList<>();

    }


    @Override
    public List<User> queryFolloweePage(@Nonnull Long userId, @Nonnull Long pageNum, @Nonnull Long pageSize) {

        QueryWrapper<SubscriberShip> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("follower_id", userId);

        List<SubscriberShip> records = subscriberShipService.page(new Page<>(pageNum, pageSize), queryWrapper).getRecords();
        if (records != null && !records.isEmpty()) {

            List<Long> followeeIds = records.stream()
                    .map(SubscriberShip::getFolloweeId)
                    .toList();

            List<User> users = listByIds(followeeIds);
            if (users != null && !users.isEmpty()) {

                return users;

            }

        }

        return new ArrayList<>();

    }


    @Override
    public List<UserVo> queryFolloweeVoPage(@Nonnull Long userId, @Nonnull Long pageNum, @Nonnull Long pageSize) {

        QueryWrapper<SubscriberShip> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("follower_id", userId);

        List<SubscriberShip> records = subscriberShipService.page(new Page<>(pageNum, pageSize), queryWrapper).getRecords();
        if (records != null && !records.isEmpty()) {

            List<Long> followeeIds = records.stream()
                    .map(SubscriberShip::getFolloweeId)
                    .toList();

            List<User> users = listByIds(followeeIds);
            if (users != null && !users.isEmpty()) {

                List<UserVo> userVos = new ArrayList<>();
                for (User user : users) {

                    UserVo userVo = new UserVo();
                    BeanUtils.copyProperties(user, userVo);

                    // 2024-11-4  16:39-单独查询当前订阅发起者与当前遍历的用户之间是否存在订阅关系(即当前请求用户是否订阅了当前遍历到的用户)
                    SubscriberShip subscriberShip = subscriberShipService.querySingleSubscription(userId, user.getId());
                    userVo.setIsSubscribed(subscriberShip != null);

                    userVos.add(userVo);

                }

                return userVos;

            }

        }

        return new ArrayList<>();

    }


    @Transactional
    @Override
    public User updateSingleUserField(@Nonnull Long userId, @Nonnull String fieldName, @Nonnull Object fieldValue) {

        // 2024-11-27  10:43-解决用户视图将某个字段重置(将值置空) , 但传null过来时会丢失fieldValue参数 , 因此这里规定 : 一旦用户传递的字段值为空串(仅包含空格的字符串也将被视为空串) , 那么表明用户想清除该字段的值(将字段的值置为null)(当然not null 字段置为空将会自动抛出异常 , 导致本次字段更新终止)
        if (fieldValue instanceof String fieldVStringValue) {

            if (!StringUtils.hasLength(fieldVStringValue.trim())) {

                fieldValue = null;

            }

        }

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", userId);

        // 2024-11-18  21:55-由于未知原因 , 导致fieldValue数据类型为LocalDateTime时 , 无控制器响应 , 因此这里规定 : 在更新birth_date字段时 , 要求其对应的字段值为LocalDateTime数据被JSON格式化后的字符串类型
        // 2024-11-18  20:04-如果当前更新的字段为用户的出生日期((当然字段类型也必须为LocalDateTime)) , 则还需要额外的同步更新用户的年龄字段 , 以达到逻辑上的一致性
        if ("birth_date".equals(fieldName)) {

            if (fieldValue == null) {

                throw new PtpException(807, "时间日期类型的字段的值不能为空!");

            }

            @SuppressWarnings("DataFlowIssue")
            LocalDateTime birthDate = JSON.parseObject((String) fieldValue, LocalDateTime.class);
            int age = Period.between(birthDate.toLocalDate(), LocalDate.now()).getYears();
            updateWrapper.set("age", age)
                    .set("birth_date", birthDate);

        } else {

            updateWrapper.set(fieldName, fieldValue);

        }

        boolean isUpdated = update(updateWrapper);
        if (isUpdated) {

            return getById(userId);

        }

        return null;

    }


    @Override
    public Map<String, Object> registerByNicknameAndPassword(@Nonnull UserLoginVo userLoginVo) {

        AssetService assetService = SpringUtil.getBean(AssetService.class);

        // 2024-11-24  11:32-需要为新创建的用户分配一个相关联的财产数据包
        Asset asset = Asset.builder()
                .balance(0.0)
                .accounts(new ArrayList<>())
                .authorities(List.of("drawback", "withdraw", "view", "update_password"))
                .credit(100.0)
                .build();

        boolean isSaveAsset = assetService.save(asset);
        if (isSaveAsset) {

            User user = User.builder()
                    .account(RandomUtils.generateAccount())
                    .password(userLoginVo.getPassword())
                    .nickname(userLoginVo.getNickname())
                    .gender(Gender.SECRET)
                    .idiograph("这个人很懒 , 什么信息也没有写~")
                    .avatar(RandomUtils.generateAvatar())
                    .background(RandomUtils.generateBackground())
                    // .roleId(1L) //// 2024-11-24  11:30-还需要提前赋予该用户角色信息(目前暂定为具备全部权限的管理员角色)
                    .roleId(2L) // 2024-12-14  13:14-还需要提前赋予该用户角色信息(默认授予新用户为普通用户角色)
                    .assetId(asset.getId()) // 2024-11-24  11:34-还要关联上刚刚创建好的财产信息
                    .build();

            boolean isSaved = save(user);
            if (isSaved) {

                return loginByNicknameAndPassword(userLoginVo); // 2024-11-24  20:05-这里为了省事 , 直接调用现成的API

            }

        }

        return null;

    }


}