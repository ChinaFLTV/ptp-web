package ptp.fltv.web.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.transfer.TransferManager;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pfp.fltv.common.constants.OAuth2LoginConstants;
import pfp.fltv.common.constants.RedisConstants;
import pfp.fltv.common.constants.WebConstants;
import pfp.fltv.common.enums.LoginClientType;
import pfp.fltv.common.exceptions.PtpException;
import pfp.fltv.common.model.po.info.AddressInfo;
import pfp.fltv.common.model.po.manage.Role;
import pfp.fltv.common.model.po.manage.User;
import pfp.fltv.common.model.vo.UserLoginVo;
import pfp.fltv.common.utils.JwtUtils;
import ptp.fltv.web.constants.CosConstants;
import ptp.fltv.web.mapper.UserMapper;
import ptp.fltv.web.service.RoleService;
import ptp.fltv.web.service.UserService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.args.GeoUnit;
import redis.clients.jedis.params.GeoSearchParam;
import redis.clients.jedis.resps.GeoRadiusResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 7:12:27
 * @description 用户服务接口的实现类
 * @filename UserServiceImpl.java
 */

@AllArgsConstructor
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    private StringRedisTemplate redisTemplate;
    private RoleService roleService;
    private Jedis jedis;
    private TransferManager transferManager;
    private COSClient cosClient;


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

        Role compactRole = Role.builder()
                .id(role.getId())
                .name(role.getName())
                .authorities(role.getAuthorities())
                .prohibition(role.getProhibition())
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
        redisTemplate.opsForValue().set(String.format("user:login:env:%s:%d", loginClientType.name().toLowerCase(), STORE_KEY), JSON.toJSONString(env), RedisConstants.CACHE_TIMEOUT, TimeUnit.MILLISECONDS);

        Map<String, Object> result = new HashMap<>();
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
    public void logout(LoginClientType clientType, Long userId) {

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


}