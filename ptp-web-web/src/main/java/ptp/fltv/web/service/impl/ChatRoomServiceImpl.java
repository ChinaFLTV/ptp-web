package ptp.fltv.web.service.impl;

import com.alibaba.fastjson2.JSON;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.transfer.TransferManager;
import jakarta.annotation.Nonnull;
import jakarta.websocket.Session;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pfp.fltv.common.model.po.manage.User;
import pfp.fltv.common.model.po.ws.ChatRoom;
import pfp.fltv.common.model.po.ws.GroupMessage;
import ptp.fltv.web.constants.CosConstants;
import ptp.fltv.web.service.ChatRoomService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/23 PM 10:51:30
 * @description 聊天室服务接口的具体实现类
 * @filename ChatRoomServiceImpl.java
 */

@Slf4j
@AllArgsConstructor
@Service
public class ChatRoomServiceImpl implements ChatRoomService {


    private TransferManager transferManager;

    private static final Map<Long, Map<String, Session>> chatRoomId2SessionsMap = new ConcurrentHashMap<>(); // 2024-6-23  23:46-存储房间号以及其当前持有的SESSION的映射 : roomId -> sessionId --> session
    private static final Map<Long, Map<Long, String>> chatRoomId2SessionIdsMap = new ConcurrentHashMap<>(); // 2024-6-25  9:11-存储用户ID与会话ID的映射 , 方便后续根据用户ID查找对应的会话 : roomId -> userId --> sessionId
    private static final Map<Long, ChatRoom> chatRoomId2ChatRoomsMap = new ConcurrentHashMap<>(); // 2024-8-23  12:41-存储房间号与房间信息的映射 : roomId -> roomInfo
    private static final Long DEFAULT_CHAT_ROOM_ID = 666L; // 2024-8-23  20:42-默认的群聊房间ID


    static {

        // 2024-8-23  13:34-默认自动设置一个公告的聊天房间
        ChatRoom defaultChatRoom = ChatRoom.builder()
                .id(DEFAULT_CHAT_ROOM_ID)
                .name("达达利亚和他的朋友们")
                .avatarUrl("https://m.qqkw.com/d/tx/titlepic/c263a882a7ed7f099e6b48961af58b0b.jpg")
                .rank(6.0)
                .onlineUsers(chatRoomId2SessionIdsMap.getOrDefault(DEFAULT_CHAT_ROOM_ID, new ConcurrentHashMap<>()).keySet())
                .build();

        chatRoomId2ChatRoomsMap.put(DEFAULT_CHAT_ROOM_ID, defaultChatRoom);

    }


    @Override
    public ChatRoom getSingleRoomInfo(@Nonnull Long roomId) {

        ChatRoom chatRoom = chatRoomId2ChatRoomsMap.get(roomId);

        Set<Long> liveUserIds = chatRoomId2SessionIdsMap.getOrDefault(roomId, new ConcurrentHashMap<>()).keySet();
        chatRoom.setOnlineUsers(liveUserIds);

        return chatRoom;

    }


    @Override
    public boolean joinChatRoom(@Nonnull Long roomId, @Nonnull Long userId, @Nonnull Session session) throws IOException {

        if (!chatRoomId2SessionsMap.containsKey(roomId)) {

            chatRoomId2SessionsMap.put(roomId, new HashMap<>());

        }
        if (!chatRoomId2SessionIdsMap.containsKey(roomId)) {

            chatRoomId2SessionIdsMap.put(roomId, new HashMap<>());

        }

        // 2024-6-24  10:27-如果先前已经存在了同ID的会话 , 则释放掉先前存在的旧会话 , 以节约服务器资源
        if (chatRoomId2SessionsMap.get(roomId).containsKey(session.getId())) {

            Session oldSession = chatRoomId2SessionsMap.get(roomId).get(session.getId());
            if (oldSession != null && oldSession.isOpen()) {

                oldSession.close();

            }

        }

        chatRoomId2SessionsMap.get(roomId).put(session.getId(), session);
        chatRoomId2SessionIdsMap.get(roomId).put(userId, session.getId());

        return true;

    }


    @Override
    public boolean sendGroupChatMsg(@Nonnull Long roomId, @Nonnull User user, @Nonnull GroupMessage groupChatMessage) throws IOException {

        String content = groupChatMessage.getContent();

        if (chatRoomId2SessionsMap.containsKey(roomId) && StringUtils.hasLength(content)) {

            Map<String, Session> sessionsMap = chatRoomId2SessionsMap.get(roomId);

            if (sessionsMap != null && !sessionsMap.isEmpty()) {

                Map<String, Object> wrappedMsgDataMap = new HashMap<>();
                wrappedMsgDataMap.put("room-id", roomId);
                wrappedMsgDataMap.put("user", user);
                wrappedMsgDataMap.put("message", groupChatMessage);

                for (Map.Entry<String, Session> entry : sessionsMap.entrySet()) {

                    // entry.getValue().getBasicRemote().sendText(String.format("[%s] : %s", groupChatMessage.getType() == GroupMessage.MessageType.SYSTEM ? "系统消息" : String.format("用户消息(%s)", user.getNickname()), content));
                    entry.getValue().getBasicRemote().sendText(JSON.toJSONString(wrappedMsgDataMap));

                }

                return true;

            }

        }

        return false;

    }


    @Override
    public boolean sendPrivateChatMsg(@Nonnull Long roomId, @Nonnull Long userId, @Nonnull GroupMessage groupChatMessage) throws IOException {

        String sessionId = chatRoomId2SessionIdsMap.get(roomId).get(userId);
        Session session = chatRoomId2SessionsMap.get(roomId).get(sessionId);

        int messageTypeCode = groupChatMessage.getMessageType().getCode();

        session.getBasicRemote().sendText(String.format("[%s] : %s", messageTypeCode >= 1703 && messageTypeCode <= 1706 ? "系统消息" : "用户消息", groupChatMessage.getContent()));
        return false;

    }


    @Override
    public void leaveChatRoom(@Nonnull Long roomId, @Nonnull Long userId, @Nonnull Session session) throws IOException {

        if (session.isOpen()) {

            session.close();

        }
        Map<String, Session> sessionsMap = chatRoomId2SessionsMap.get(roomId);
        chatRoomId2SessionsMap.get(roomId).remove(session.getId());
        chatRoomId2SessionIdsMap.get(roomId).remove(userId);

    }


    @Override
    public String uploadMediaFile(@Nonnull Long messageId, @Nonnull GroupMessage.ContentType contentType, @Nonnull MultipartFile file) {

        try {

            String fileName = Objects.requireNonNull(file.getOriginalFilename());
            // 2024-9-5  20:00-腾讯云COS支持文件名称最长为100个字符 , 因此这里在已有的限制条件上进一步进行限制 , 最多支持80个字符长度的文件名称 , 超出则进行截断处理
            String key = CosConstants.BUCKET_CHAT_ROOM_FILE + "/" + messageId + "-" + (fileName.length() > 80 ? fileName.substring(0, 80) : fileName);

            String bucketName = CosConstants.BUCKET_CHAT_ROOM;

            switch (contentType) {

                case PHOTO -> key = CosConstants.BUCKET_CHAT_ROOM_PHOTO + "/" + messageId + ".png";
                case VIDEO -> key = CosConstants.BUCKET_CHAT_ROOM_VIDEO + "/" + messageId + ".mp4";
                case AUDIO -> key = CosConstants.BUCKET_CHAT_ROOM_AUDIO + "/" + messageId + ".mp3";
                case VOICE -> key = CosConstants.BUCKET_CHAT_ROOM_VOICE + "/" + messageId + ".wav";

            }

            PutObjectRequest request = new PutObjectRequest(bucketName, key, file.getInputStream(), null);

            // 2024-9-2  21:19-高级接口会返回一个异步结果Upload , 可同步地调用 waitForUploadResult 方法等待上传完成，成功返回 UploadResult, 失败抛出异常
            UploadResult uploadResult = transferManager.upload(request).waitForUploadResult();

            return String.format(CosConstants.PUBLIC_REQUEST_URL_PREFIX, bucketName, key);

        } catch (CosClientException | InterruptedException | IOException ex) {

            log.error("上传聊天室产生的多媒体文件到云端COS中失败 : {}", ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());
            return null;

        }

    }


}