package ptp.fltv.web.service;

import jakarta.annotation.Nonnull;
import jakarta.websocket.Session;

import java.io.IOException;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/23 PM 10:50:59
 * @description 聊天室服务接口
 * @filename ChatRoomService.java
 */

public interface ChatRoomService {


    /**
     * @param session 要添加的会话
     * @param roomId  房间号
     * @return 新会话是否添加成功
     * @author Lenovo/LiGuanda
     * @date 2024/6/23 PM 11:57:02
     * @version 1.0.0
     * @description 添加会话到指定聊天室中
     * @filename ChatRoomService.java
     */
    boolean addSessionToChatRoom(@Nonnull Long roomId, @Nonnull Session session);


    /**
     * @param customMsgBody 自定义协议的消息体(其中可能会包含房间号、用户ID、聊天消息等等)
     * @return 群聊消息是否发送成功
     * @throws IOException 消息发送过程中如果出现IO异常将抛出此错误
     * @author Lenovo/LiGuanda
     * @date 2024/6/23 PM 11:00:16
     * @version 1.0.0
     * @description 向指定房间号的聊天室发送群聊消息
     * @filename ChatRoomService.java
     */
    boolean sendGroupChatMsg(@Nonnull String customMsgBody) throws IOException;


}