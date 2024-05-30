package ptp.fltv.web.mq;

import jakarta.annotation.Nonnull;

import java.io.Serializable;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/30 PM 11:02:16
 * @description MQ助手类的接口规范
 * @filename MqHelper.java
 */

public interface MqHelper {


    void sendMsg(@Nonnull Serializable data);


}