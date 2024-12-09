package com.fltv.web.service.monitor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.*;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/12/9 PM 9:34:38
 * @description Redis数据库相关的个性化配置类
 * @filename RedisConfig.java
 */

@Configuration
public class RedisConfig {


    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private Integer port;
    @Value("${spring.data.redis.password}")
    private String password;


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/12/9 PM 9:44:29
     * @version 1.0.0
     * @description 自定义的Redis客户端(Jedis)
     * @filename RedisConfig.java
     */
    @Bean
    public Jedis jedis() {

        JedisClientConfig jedisClientConfig = DefaultJedisClientConfig.builder()
                .password(password)
                .build();

        return new Jedis(host, port, jedisClientConfig);

    }


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/12/9 PM 10:21:02
     * @version 1.0.0
     * @description 自定义的Redis连接池客户端(JedisPool)
     * @filename RedisConfig.java
     */
    @Bean
    public JedisPool jedisPool() {

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(100); // 最大连接数
        poolConfig.setMaxIdle(10); // 最大空闲连接数
        poolConfig.setJmxEnabled(false); // 2024-12-9  22:48-解决应用启动时报 Unable to register MBean [JedisPool [maxTotal=100, blockWhenExhausted=true... 异常而无法启动的问题(参考自 https://github.com/redis/jedis/issues/2781)

        return new JedisPool(poolConfig, host, port, 0, password); // 2024-12-9  22:34-超时时间设置为0则表示不设置超时时间

    }


}