package ptp.fltv.web.mq.impl;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;
import ptp.fltv.web.mq.MqHelper;

import java.io.Serializable;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/30 PM 11:03:37
 * @description MQ助手类的接口规范的RocketMQ实现
 * @filename RocketMqHelper.java
 */

@AllArgsConstructor
@Component
public class RocketMqHelper implements MqHelper {


    private StreamBridge streamBridge;


    @Override
    public void sendMsg(@Nonnull Serializable data) {

        boolean send = streamBridge.send("producer1-out-0", MessageBuilder.withPayload(data).build());
        System.out.println("-----------------------------------sendMsg------------------------------");
        System.out.println("send = " + send);

    }


}