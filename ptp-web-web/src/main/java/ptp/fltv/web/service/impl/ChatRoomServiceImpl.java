package ptp.fltv.web.service.impl;

import jakarta.annotation.Nonnull;
import jakarta.websocket.Session;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pfp.fltv.common.model.po.manage.User;
import pfp.fltv.common.model.po.ws.GroupChatMessage;
import ptp.fltv.web.service.ChatRoomService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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


    private static final Map<Long, Map<String, Session>> chatRoom2SessionsMap = new ConcurrentHashMap<>(); // 2024-6-23  23:46-存储房间号以及其当前持有的SESSION的映射 : roomId -> sessionId --> session
    private static final Map<Long, Map<Long, String>> chatRoom2SessionIdsMap = new ConcurrentHashMap<>(); // 2024-6-25  9:11-存储用户ID与会话ID的映射 , 方便后续根据用户ID查找对应的会话 : roomId -> userId --> sessionId


    @Override
    public Map<String, Object> getSingleRoomInfo(@Nonnull Long roomId) {

        Map<String, Object> roomInfo = new HashMap<>();

        roomInfo.put("live-count", chatRoom2SessionsMap.get(roomId).size());
        roomInfo.put("live-user-ids", chatRoom2SessionIdsMap.get(roomId).keySet());

        return roomInfo;

    }


    @Override
    public boolean joinChatRoom(@Nonnull Long roomId, @Nonnull Long userId, @Nonnull Session session) throws IOException {

        if (!chatRoom2SessionsMap.containsKey(roomId)) {

            chatRoom2SessionsMap.put(roomId, new HashMap<>());

        }
        if (!chatRoom2SessionIdsMap.containsKey(roomId)) {

            chatRoom2SessionIdsMap.put(roomId, new HashMap<>());

        }

        // 2024-6-24  10:27-如果先前已经存在了同ID的会话 , 则释放掉先前存在的旧会话 , 以节约服务器资源
        if (chatRoom2SessionsMap.get(roomId).containsKey(session.getId())) {

            Session oldSession = chatRoom2SessionsMap.get(roomId).get(session.getId());
            if (oldSession != null && oldSession.isOpen()) {

                oldSession.close();

            }

        }

        chatRoom2SessionsMap.get(roomId).put(session.getId(), session);
        chatRoom2SessionIdsMap.get(roomId).put(userId, session.getId());

        return true;

    }


    @Override
    public boolean sendGroupChatMsg(@Nonnull Long roomId, @Nonnull User user, @Nonnull GroupChatMessage groupChatMessage) throws IOException {

        String msg = groupChatMessage.getMsg();

        if (chatRoom2SessionsMap.containsKey(roomId) && StringUtils.hasLength(msg)) {

            Map<String, Session> sessionsMap = chatRoom2SessionsMap.get(roomId);

            if (sessionsMap != null && !sessionsMap.isEmpty()) {

                for (Map.Entry<String, Session> entry : sessionsMap.entrySet()) {

                    entry.getValue().getBasicRemote().sendText(String.format("[%s] : %s", groupChatMessage.isSystem() ? "系统消息" : String.format("用户消息(%s)", user.getNickname()), msg));

                }

                return true;

            }

        }

        return false;

    }


    @Override
    public boolean sendPrivateChatMsg(@Nonnull Long roomId, @Nonnull Long userId, @Nonnull GroupChatMessage groupChatMessage) throws IOException {

        String sessionId = chatRoom2SessionIdsMap.get(roomId).get(userId);
        Session session = chatRoom2SessionsMap.get(roomId).get(sessionId);

        session.getBasicRemote().sendText(String.format("[%s] : %s", groupChatMessage.isSystem() ? "系统消息" : "用户消息", groupChatMessage.getMsg()));
        return false;

    }


    @Override
    public void leaveChatRoom(@Nonnull Long roomId, @Nonnull Long userId, @Nonnull Session session) throws IOException {

        if (session.isOpen()) {

            session.close();

        }
        Map<String, Session> sessionsMap = chatRoom2SessionsMap.get(roomId);
        chatRoom2SessionsMap.get(roomId).remove(session.getId());
        chatRoom2SessionIdsMap.get(roomId).remove(userId);

    }


}