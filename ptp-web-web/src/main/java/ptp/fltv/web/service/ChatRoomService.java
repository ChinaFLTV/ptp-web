package ptp.fltv.web.service;

import jakarta.annotation.Nonnull;
import jakarta.websocket.Session;
import pfp.fltv.common.model.po.manage.User;
import ptp.fltv.web.ws.GroupChatMessage;

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
     * @throws IOException 关闭旧Session时出现IO错误将会抛出此异常
     * @author Lenovo/LiGuanda
     * @date 2024/6/23 PM 11:57:02
     * @version 1.0.0
     * @description 添加会话到指定聊天室中
     * @filename ChatRoomService.java
     */
    boolean addSessionToChatRoom(@Nonnull Long roomId, @Nonnull Session session) throws IOException;


    /**
     * @param groupChatMessage 群聊消息
     * @param roomId           当前房间号
     * @param user             发送消息用户的ID
     * @return 群聊消息是否发送成功
     * @throws IOException 消息发送过程中如果出现IO异常将抛出此错误
     * @author Lenovo/LiGuanda
     * @date 2024/6/23 PM 11:00:16
     * @version 1.0.0
     * @description 向指定房间号的聊天室发送群聊消息
     * @filename ChatRoomService.java
     */
    boolean sendGroupChatMsg(@Nonnull Long roomId, @Nonnull User user, @Nonnull GroupChatMessage groupChatMessage) throws IOException;


    /**
     * @param roomId  指定的房间号
     * @param session 待删除的会话
     * @throws IOException 关闭WebSocket连接时出现IO错误将会抛出此异常
     * @author Lenovo/LiGuanda
     * @date 2024/6/24 PM 11:01:48
     * @version 1.0.0
     * @description 移除掉指定房间中的指定会话
     * @filename ChatRoomService.java
     */
    void removeSession(@Nonnull Long roomId, @Nonnull Session session) throws IOException;


}