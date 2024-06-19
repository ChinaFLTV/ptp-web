package ptp.fltv.web.mq;

import jakarta.annotation.Nonnull;
import pfp.fltv.common.model.base.content.BaseEntity;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/19 AM 10:18:11
 * @description 内容实体排行的MQ服务接口规范
 * @filename ContentRankMqService.java
 */

public interface ContentRankMqService {


    /**
     * @param <T>         内容类型的泛型
     * @param contentObj  内容实体数据
     * @param contentType 内容实体所属类型
     * @author Lenovo/LiGuanda
     * @date 2024/6/19 PM 9:32:28
     * @version 1.0.0
     * @description 发送内容实体五项指标变化的消息到RocketMQ
     * @filename ContentRankMqService.java
     */
    <T extends BaseEntity> void sendIndexChangeMsg(@Nonnull T contentObj, @Nonnull BaseEntity.ContentType contentType);


}