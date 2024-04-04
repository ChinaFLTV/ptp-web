package ptp.fltv.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/4 下午 8:48:21
 * @description 验证Redis配置是否正确以及能否与Redis客户端进行正常交互
 * @filename RedisTemplateTest.java
 */

@SpringBootTest
public class RedisTemplateTest {


    @Autowired
    private StringRedisTemplate redisTemplate;


    @Test
    public void test() {

        System.out.println(redisTemplate.opsForValue().get("k1"));

    }


}
