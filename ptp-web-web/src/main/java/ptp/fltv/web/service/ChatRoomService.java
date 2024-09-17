package ptp.fltv.web.service;

import jakarta.annotation.Nonnull;
import jakarta.websocket.Session;
import org.springframework.web.multipart.MultipartFile;
import pfp.fltv.common.model.po.manage.User;
import pfp.fltv.common.model.po.ws.ChatRoom;
import pfp.fltv.common.model.po.ws.GroupMessage;

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
     * @param roomId 指定的房间号ID
     * @return 单个房间的实时详细状态信息(不存在指定房间则返回null)
     * @author Lenovo/LiGuanda
     * @date 2024/6/25 AM 9:00:59
     * @version 1.0.0
     * @description 获取单个房间的实时详细状态信息
     * @filename ChatRoomService.java
     */
    ChatRoom getSingleRoomInfo(@Nonnull Long roomId);


    /**
     * @param session 要添加的会话
     * @param userId  用户ID
     * @param roomId  房间号
     * @return 新会话是否添加成功
     * @throws IOException 关闭旧Session时出现IO错误将会抛出此异常
     * @author Lenovo/LiGuanda
     * @date 2024/6/23 PM 11:57:02
     * @version 1.0.0
     * @description 添加会话到指定聊天室中
     * @filename ChatRoomService.java
     */
    boolean joinChatRoom(@Nonnull Long roomId, @Nonnull Long userId, @Nonnull Session session) throws IOException;


    /**
     * @param groupChatMessage 群聊消息
     * @param roomId           当前房间号
     * @param user             发送消息用户
     * @return 群聊消息是否发送成功
     * @throws IOException 消息发送过程中如果出现IO异常将抛出此错误
     * @author Lenovo/LiGuanda
     * @date 2024/6/23 PM 11:00:16
     * @version 1.0.0
     * @description 向指定房间号的聊天室发送群聊消息
     * @filename ChatRoomService.java
     */
    boolean sendGroupChatMsg(@Nonnull Long roomId, @Nonnull User user, @Nonnull GroupMessage groupChatMessage) throws IOException;


    /**
     * @param groupChatMessage 群聊消息
     * @param roomId           当前房间号
     * @param userId           发送消息用户的ID
     * @return 私聊消息是否发送成功
     * @throws IOException 消息发送过程中如果出现IO异常将抛出此错误
     * @author Lenovo/LiGuanda
     * @date 2024/6/25 AM 8:38:23
     * @version 1.0.0
     * @description 发送单条消息到同房间内的指定用户
     * @filename ChatRoomService.java
     */
    boolean sendPrivateChatMsg(@Nonnull Long roomId, @Nonnull Long userId, @Nonnull GroupMessage groupChatMessage) throws IOException;


    /**
     * @param roomId  指定的房间号
     * @param userId  用户ID
     * @param session 待删除的会话
     * @throws IOException 关闭WebSocket连接时出现IO错误将会抛出此异常
     * @author Lenovo/LiGuanda
     * @date 2024/6/24 PM 11:01:48
     * @version 1.0.0
     * @description 移除掉指定房间中的指定会话
     * @filename ChatRoomService.java
     */
    void leaveChatRoom(@Nonnull Long roomId, @Nonnull Long userId, @Nonnull Session session) throws IOException;


    /**
     * @param messageId   消息ID
     * @param contentType 消息内容类型
     * @param file        消息数据(多媒体内容)
     * @return 上传成功则返回该资源的云端访问直链 , 失败则返回null
     * @pa
     * @author Lenovo/LiGuanda
     * @date 2024/8/30 PM 11:51:52
     * @version 1.0.0
     * @description 上传聊天消息的多媒体数据到云端COS对象存储中
     * @filename ChatRoomService.java
     */
    String uploadMediaFile(@Nonnull Long messageId, @Nonnull GroupMessage.ContentType contentType, @Nonnull MultipartFile file);


    /**
     * @param id 聊天室ID
     * @return 查询出来的聊天室信息
     * @author Lenovo/LiGuanda
     * @date 2024/9/10 PM 10:06:37
     * @version 1.0.0
     * @description 根据聊天室ID查询对应的聊天室信息
     * @filename ChatRoomService.java
     */
    ChatRoom querySingleChatRoomById(@Nonnull Long id);


}