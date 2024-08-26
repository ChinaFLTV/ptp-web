package ptp.fltv.web.service.impl;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Nonnull;
import jakarta.websocket.Session;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pfp.fltv.common.model.po.manage.User;
import pfp.fltv.common.model.po.ws.ChatRoom;
import pfp.fltv.common.model.po.ws.GroupMessage;
import ptp.fltv.web.service.ChatRoomService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/23 PM 10:51:30
 * @description 聊天室服务接口的具体实现类
 * @filename ChatRoomServiceImpl.java
 */

@Service
public class ChatRoomServiceImpl implements ChatRoomService {


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

        int messageTypeCode = groupChatMessage.getType().getCode();

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


}