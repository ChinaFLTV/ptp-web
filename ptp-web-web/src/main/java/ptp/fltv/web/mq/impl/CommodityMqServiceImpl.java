package ptp.fltv.web.mq.impl;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;
import ptp.fltv.web.mq.CommodityMqService;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/1 PM 10:05:41
 * @description 商品的MQ服务接口规范的实现
 * @filename CommodityMqServiceImpl.java
 */

@Slf4j
@AllArgsConstructor
@Service
public class CommodityMqServiceImpl implements CommodityMqService {


    private RocketMQTemplate rocketMQTemplate;


    @Override
    public void asyncSendOrderAddMsg(@Nonnull String topic, @Nonnull Serializable payload, @Nullable Consumer<SendResult> onSuccess, @Nullable Consumer<Throwable> onException) {

        rocketMQTemplate.asyncSend(topic, payload, new SendCallback() {


            @Override
            public void onSuccess(SendResult sendResult) {

                log.info("Record add message sent successfully ! result = {}", sendResult.getSendStatus().name());
                if (onSuccess != null) {

                    onSuccess.accept(sendResult);

                }

            }


            @Override
            public void onException(Throwable ex) {

                log.error("Record add message sent failed ! result = {}", ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());
                if (onException != null) {

                    onException.accept(ex);

                }

            }


        });

    }


}