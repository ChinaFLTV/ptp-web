package com.fltv.web.service.monitor.service.impl;

import com.fltv.web.service.monitor.service.RedisService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/12/9 PM 9:27:14
 * @description Redis数据库监控服务接口实现
 * @filename RedisServiceImpl.java
 */

@RequiredArgsConstructor
@Service
public class RedisServiceImpl implements RedisService {


    private final JedisPool jedisPool;


    @Override
    public Map<String, Object> queryInfoById(@Nonnull Long id) {

        Map<String, Object> map = new HashMap<>();

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


}