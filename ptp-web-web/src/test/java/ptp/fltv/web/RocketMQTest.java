package ptp.fltv.web;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.support.MessageBuilder;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/31 PM 11:03:55
 * @description 测试RocketMQ消息发送连通性
 * @filename RocketMQTest.java
 */

@SpringBootTest
public class RocketMQTest {


    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Test
    public void testSendMsg() {

        System.out.println("----------------------------------testSendMsg------START-------------------------");
        rocketMQTemplate.send("test-topic-1", MessageBuilder.withPayload("Hello, World! I'm from spring message").build());
        System.out.println("----------------------------------testSendMsg----END---------------------------");

    }


}