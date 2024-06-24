package ptp.fltv.web.ws;

import java.time.LocalDateTime;

/**
 * @param isSystem 是否是系统消息
 * @param msg      真正的消息内容
 * @param dateTime 消息产生时间
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/24 PM 11:45:52
 * @description WebSocket中的群聊消息
 * @filename GroupChatMessage.java
 */

public record GroupChatMessage(boolean isSystem, String msg, LocalDateTime dateTime) {


}