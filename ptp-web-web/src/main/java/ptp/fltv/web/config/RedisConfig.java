package ptp.fltv.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.Jedis;

import java.time.Duration;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/23 PM 8:51:12
 * @description Redis相关的自定义配置类
 * @filename RedisConfig.java
 */

@Configuration
public class RedisConfig {


    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.username}")
    private String username;
    @Value("${spring.data.redis.password}")
    private String password;
    @Value("${spring.data.redis.connect-timeout}")
    private Duration connectionTimeout;
    @Value("${spring.data.redis.jedis.pool.min-idle}")
    private int minIdle;
    @Value("${spring.data.redis.jedis.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.data.redis.jedis.pool.max-wait}")
    private long maxWaitMillis;


    /**
     * @return 自定义配置的Jedis客户端
     * @author Lenovo/LiGuanda
     * @date 2024/6/23 PM 9:52:46
     * @version 1.0.0
     * @description 自定义配置的Jedis客户端
     * @filename RedisConfig.java
     */
    @Bean
    public Jedis jedis() {

        DefaultJedisClientConfig jedisClientConfig = DefaultJedisClientConfig.builder()
                .connectionTimeoutMillis(Math.toIntExact(connectionTimeout.toMillis()))
                .user(username)
                .password(password)
                .build();
        return new Jedis(host, port, jedisClientConfig);

    }


}