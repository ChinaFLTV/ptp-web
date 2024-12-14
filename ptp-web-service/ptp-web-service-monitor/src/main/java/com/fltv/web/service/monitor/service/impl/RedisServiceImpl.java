package com.fltv.web.service.monitor.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.fltv.web.service.monitor.model.po.RedisKeyValueInfo;
import com.fltv.web.service.monitor.service.RedisService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.args.GeoUnit;
import redis.clients.jedis.resps.GeoRadiusResponse;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/12/9 PM 9:27:14
 * @description Redis数据库监控服务接口实现
 * @filename RedisServiceImpl.java
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisServiceImpl implements RedisService {


    private final JedisPool jedisPool;


    @Override
    public Map<String, Object> queryInfoById(@Nonnull Long id) {

        Map<String, Object> map = new LinkedHashMap<>(); // 2024-12-12  15:25-保证返回的数据在交由前端进行遍历时 , 能够保证与现有的配置数据的顺序保持一致

        Jedis jedis = jedisPool.getResource();
        String infoStr = jedis.info();

        if (StringUtils.hasLength(infoStr)) {

            Arrays.stream(infoStr.split("\n"))
                    .map(String::trim)
                    .filter(it -> StringUtils.hasLength(it) && !it.startsWith("#"))
                    .forEach(it -> {

                        String[] parts = it.split(":");
                        if (parts.length == 2) {

                            map.put(parts[0], parts[1]);

                        } else if (parts.length == 1) {

                            map.put(parts[0], "");

                        }

                    });

        }

        return map;

    }


    @Override
    public Object runSingleCommand(@Nonnull Long id, @Nonnull String command) {

        if ("\"\"".equals(command.trim())) {

            return null;

        }

        // 2024-12-12  13:53-如果你使用API fox进行测试时 , 这里传入的命令是不会加引号的 , 因此这里需要根据具体情况进行选择性去除
        if (command.startsWith("\"") && command.endsWith("\"")) {

            command = command.trim().substring(1, command.length() - 1); // 2024-12-12  13:49-由于传递过来的命令会被自动在头部和尾部加了引号 , 因此这里需要去掉两端的双引号

        }

        Jedis jedis = jedisPool.getResource();

        // 2024-12-10  1:18-由于 redis.call 方法不能接受整条命令作为一个参数传进来 , 而是需要按照空格切分成多个部分作为多个参数传进来才行
        String[] parts = command.split("\\s+");

        StringBuilder builder = new StringBuilder("return redis.call(");

        for (int i = 0; i < parts.length; i++) {

            builder.append("'")
                    .append(parts[i])
                    .append("'");

            if (i < parts.length - 1) {

                builder.append(",");

            }

        }

        builder.append(");");

        return jedis.eval(builder.toString());

    }


    @Override
    public Map<String, Object> queryAllKeyValuePairsById(@Nonnull Long id) {

        Map<String, Object> map = new HashMap<>();

        Jedis jedis = jedisPool.getResource();
        Set<String> keys = jedis.keys("*");

        if (keys != null && !keys.isEmpty()) {

            for (String key : keys) {

                String type = jedis.type(key);
                switch (type) {

                    case "string" -> {

                        String string = jedis.get(key);

                        try {

                            map.put(key, string == null ? null : JSON.parseObject(string));

                        } catch (Exception ex) {

                            // 2024-12-13  13:30-这里出现反序列化失败大概率是因为字符串本身的value值就不是对象的序列化字符串 , 很可能就是基本数据类型的转换的字符串 , 所以出现这种情况 , 直接可以将此键值对存入映射中即可
                            log.error("反序列化指定Redis数据库(ID = {})的 key( = {}) 对应的 value( = {}) 为对象时出现异常 , 将调整插入策略为 直接插入 : {}", id, key, string, ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());
                            map.put(key, string);

                        }

                    }

                    case "list" -> {

                        List<JSONObject> list = jedis.lrange(key, 0, -1)
                                .stream()
                                .filter(StringUtils::hasLength)
                                .map(JSON::parseObject)
                                .toList();
                        map.put(key, list);

                    }

                    case "hash" -> {

                        Map<String, JSONObject> hash = new HashMap<>();
                        jedis.hgetAll(key).forEach((key1, value) -> map.put(key1, JSON.parseObject(value)));
                        map.put(key, hash);

                    }

                    case "set" -> {

                        Set<JSONObject> set = jedis.smembers(key)
                                .stream()
                                .filter(StringUtils::hasLength)
                                .map(JSON::parseObject)
                                .collect(Collectors.toSet());
                        map.put(key, set);

                    }

                    case "zset" -> {

                        List<JSONObject> zset = jedis.zrange(key, 0, -1)
                                .stream()
                                .filter(StringUtils::hasLength)
                                .map(JSON::parseObject)
                                .toList();
                        map.put(key, zset);

                    }

                    case "geo" -> {

                        List<String> geo = jedis.georadius(key, 0, 0, Double.MAX_VALUE, GeoUnit.KM)
                                .stream()
                                .map(JSON::toJSONString)
                                .toList();
                        map.put(key, geo);

                    }

                    default -> log.warn("获取指定Redis数据库(ID = {})的全部 key-value 键值对时出现跳过情况 : 跳过了 key = {} , value = {}", id, key, type);


                }

            }

        }

        return map;

    }


    @Override
    public RedisKeyValueInfo querySingleKeyValuePairInfo(@Nonnull Long id, @Nonnull String key) {

        RedisKeyValueInfo info = new RedisKeyValueInfo();
        info.setKey(key);

        if (!StringUtils.hasLength(key)) {

            return info;

        }

        Jedis jedis = jedisPool.getResource();

        boolean isExist = jedis.exists(key);
        info.setExist(isExist);

        if (isExist) {

            String type = jedis.type(key);
            info.setType(type);
            info.setTtl(jedis.ttl(key)); // 2024-12-14  14:16-ttl 方法返回的数值的单位为秒 , 数值为-1则表示永不过期 , 数值为-2则表示值不存在

            switch (type) {

                case "string" -> {

                    info.setValue(jedis.get(key));
                    info.setCount(1L);
                    info.setSize(jedis.strlen(key)); // 2024-12-14  14:31-数值的单位为Byte

                }

                case "list" -> {

                    info.setValue(jedis.lrange(key, 0, -1));
                    info.setCount(jedis.llen(key));

                }

                case "hash" -> {

                    info.setValue(jedis.hgetAll(key));
                    info.setCount(jedis.hlen(key));

                }

                case "set" -> {

                    info.setValue(jedis.smembers(key));
                    info.setCount(jedis.scard(key));

                }

                case "zset" -> {

                    info.setValue(jedis.zrange(key, 0, -1));
                    info.setCount(jedis.zcard(key));

                }

                case "geo" -> {

                    List<GeoRadiusResponse> geo = jedis.georadius(key, 0, 0, Double.MAX_VALUE, GeoUnit.KM);

                    info.setValue(geo);
                    info.setCount(geo.size());

                }

            }


        }

        return info;

    }


    @Override
    public boolean insertSingleKeyValuePair(Long id, RedisKeyValueInfo info) {

        return updateSingleKeyValuePair(id, null, info);

    }


    @Override
    public boolean updateSingleKeyValuePair(@Nonnull Long id, @Nullable String oldKey, @Nonnull RedisKeyValueInfo info) {

        Jedis jedis = jedisPool.getResource();

        // 2024-12-14  16:01-我们更新键值对的思路 : 添加新的key-value -> 验证key-value是否存在 -> 设置TTL -> 删除旧key-value
        if (StringUtils.hasLength(info.getKey()) && info.getValue() != null) {

            jedis.set(info.getKey(), info.getValue().toString());

            if (jedis.exists(info.getKey())) {

                if (/*info.getTtl() == -1 ||*/ info.getTtl() > 0) { // 2024-12-14  16:23-如果用户指定键值对为永不过期(-1) , 则这里可以直接跳过设置 , 因为刚插入的键值对的默认过期时间就为-1(永不过期)

                    jedis.expire(info.getKey(), info.getTtl());
                    if (oldKey != null && !oldKey.equals(info.getKey())) { // 2024-12-14  16:49-只有在新key与旧key不一致的情况下才去单独的移除旧key , 若二者一致 , 则旧key直接被新key覆盖掉了 , 这里就没必要再专门删除了

                        jedis.del(oldKey);

                    }
                    return true;

                } else if (info.getTtl() == -1) {

                    if (oldKey != null && !oldKey.equals(info.getKey())) { // 2024-12-14  16:53-只有在新key与旧key不一致的情况下才去单独的移除旧key , 若二者一致 , 则旧key直接被新key覆盖掉了 , 这里就没必要再专门删除了

                        jedis.del(oldKey);

                    }
                    return true;

                }

            }

        }

        return false;

    }


    @Override
    public long deleteSingleKeyValuePair(@Nonnull Long id, @Nonnull String key) {

        if (!StringUtils.hasLength(key)) {

            return 0;

        }

        Jedis jedis = jedisPool.getResource();
        return jedis.del(key);

    }


}