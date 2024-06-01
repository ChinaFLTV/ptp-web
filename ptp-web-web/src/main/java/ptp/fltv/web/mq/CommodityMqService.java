package ptp.fltv.web.mq;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.rocketmq.client.producer.SendResult;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/1 PM 10:05:00
 * @description 商品的MQ服务接口规范
 * @filename CommodityMqService.java
 */

public interface CommodityMqService {


    /**
     * @param topic       所要发送消息到的主题
     * @param payload     消息数据(必须可序列化)
     * @param onSuccess   消息发送成功的异步回调(可为空)
     * @param onException 消息发送失败的异步回调(可为空)
     * @author Lenovo/LiGuanda
     * @date 2024/6/1 PM 10:06:49
     * @version 1.0.0
     * @description 异步发送订单添加消息
     * @filename CommodityMqService.java
     */
    void asyncSendOrderAddMsg(@Nonnull String topic, @Nonnull Serializable payload, @Nullable Consumer<SendResult> onSuccess, @Nullable Consumer<Throwable> onException);


}