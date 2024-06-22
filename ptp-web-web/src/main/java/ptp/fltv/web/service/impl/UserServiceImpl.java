package ptp.fltv.web.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.dao.DataAccessException;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Service;
import pfp.fltv.common.constants.OAuth2LoginConstants;
import pfp.fltv.common.constants.RedisConstants;
import pfp.fltv.common.enums.LoginClientType;
import pfp.fltv.common.exceptions.PtpException;
import pfp.fltv.common.model.po.info.AddressInfo;
import pfp.fltv.common.model.po.manage.Role;
import pfp.fltv.common.model.po.manage.User;
import pfp.fltv.common.model.vo.UserLoginVo;
import pfp.fltv.common.utils.JwtUtils;
import ptp.fltv.web.mapper.UserMapper;
import ptp.fltv.web.service.RoleService;
import ptp.fltv.web.service.UserService;

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


    @Override
    public User getUserByNickname(@NonNull String nickname) {

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickname, nickname);

        return baseMapper.selectOne(queryWrapper);

    }


    @Override
    public Map<String, Object> login(@Nonnull UserLoginVo userLoginVo) throws PtpException {

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
                .roleId(user.getId())
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
        result.put("user_login_data", jwt1);
        String jwt2 = JwtUtils.encode(STORE_KEY);
        result.put("store_key", jwt2);

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
            user.setAddressInfoId(null);
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

        GeoReference<String> point = GeoReference.fromCoordinate(longitude, latitude);
        Distance range = new Distance(radius, Metrics.KILOMETERS);
        RedisGeoCommands.GeoRadiusCommandArgs commandArgs = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                .includeCoordinates()
                .sortAscending()
                .limit(limit);

        // final Map<Long, Double> id2distanceMap = new HashMap<>();
        final Map<Integer, List<User>> distance2UserMap = new HashMap<>();

        AddressInfo addressInfo = new AddressInfo();
        addressInfo.setLongitude(longitude);
        addressInfo.setLatitude(latitude);


        User currentRefreshedUser = refreshGeolocation(id, addressInfo);

        if (currentRefreshedUser == null) {

            throw new PtpException(811, "无法更新当前用户的位置信息", "执行 UserServiceImpl::refreshGeolocation 方法后返回的user为null");

        }

        // 2024-6-23  00:08-由于Redis的Pipeline并不能保证输入到Redis服务端的这一串命令原子化执行 , 因此我们需要使用Redis的事务来完成 附近的人 距离计算操作
        redisTemplate.executePipelined(new SessionCallback<>() {


            @SuppressWarnings("unchecked")
            @Override
            public <K, V> Object execute(@Nonnull RedisOperations<K, V> operations) throws DataAccessException {

                try {

                    Objects.requireNonNull(operations.opsForGeo().search((K) KEY, (GeoReference<V>) point, range, commandArgs))
                            .getContent()
                            .forEach(res -> {

                                User peopleNearby = JSON.parseObject((String) res.getContent().getName(), User.class);
                                Distance distance = operations.opsForGeo().distance((K) KEY, (V) JSON.toJSONString(currentRefreshedUser, JSONWriter.Feature.NotWriteDefaultValue, JSONWriter.Feature.NotWriteEmptyArray), res.getContent().getName(), Metrics.KILOMETERS);
                                // id2distanceMap.put(Long.parseLong(id), Objects.requireNonNull(distance).getValue());

                                if (peopleNearby != null && distance != null) {

                                    // 2024-6-22  23:22-不要为了节约内存空间而把extension单独抽离到 forEach 外面!!!这样会使得全部的User最终都引用到了同一个最后一个User更新后的meta映射!!!
                                    Map<String, Object> extension = new HashMap<>();
                                    extension.put("distance", distance.getValue());
                                    peopleNearby.setMeta(extension);

                                    int estimatedDistance = ((int) distance.getValue() / 10) * 10;

                                    if (!distance2UserMap.containsKey(estimatedDistance)) {

                                        distance2UserMap.put(estimatedDistance, new ArrayList<>());

                                    }
                                    distance2UserMap.get(estimatedDistance).add(peopleNearby);

                                }

                            });

                } catch (Exception e) {

                    e.printStackTrace();
                    System.out.println();

                }

                return null;

            }


        });

        /*if (id2distanceMap.isEmpty()) {

            return Collections.emptyMap();

        }*/

        // 2024-6-22  20:17-下面是及其消耗时间且性价比极低的操作:
        // 消耗时间长 : 从数据库查询数据 , 这是磁盘IO操作 , 且ID大概率不会是连续的 , 因此是随机磁盘IO
        // 性价比极低 : 由于读操作时间很长 , 因此在这个过程中查询出来的数据并不总是最新的有效的
        /*List<User> users = baseMapper.selectBatchIds(id2distanceMap.keySet());
        for (User user : users) {

            if (!distance2UserMap.containsKey(id2distanceMap.get(user.getId()))) {

                distance2UserMap.put(id2distanceMap.get(user.getId()), new ArrayList<>());

            }

            distance2UserMap.get(id2distanceMap.get(user.getId())).add(user);

        }*/

        return distance2UserMap;

    }


}