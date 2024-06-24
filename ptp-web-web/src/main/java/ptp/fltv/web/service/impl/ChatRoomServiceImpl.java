package ptp.fltv.web.service.impl;

import jakarta.annotation.Nonnull;
import jakarta.websocket.Session;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pfp.fltv.common.model.po.manage.User;
import ptp.fltv.web.service.ChatRoomService;
import ptp.fltv.web.ws.GroupChatMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/23 PM 10:51:30
 * @description 聊天室服务接口的具体实现类
 * @filename ChatRoomServiceImpl.java
 */

@Service
public class ChatRoomServiceImpl implements ChatRoomService {


    private static final Map<Long, Map<String, Session>> chatRoom2SessionsMap = new HashMap<>(); // 2024-6-23  23:46-存储房间号以及其当前持有的SESSION的映射


    @Override
    public boolean addSessionToChatRoom(@Nonnull Long roomId, @Nonnull Session session) throws IOException {

        if (!chatRoom2SessionsMap.containsKey(roomId)) {

            chatRoom2SessionsMap.put(roomId, new HashMap<>());

        }

        // 2024-6-24  10:27-如果先前已经存在了同ID的会话 , 则释放掉先前存在的旧会话 , 以节约服务器资源
        if (chatRoom2SessionsMap.get(roomId).containsKey(session.getId())) {

            Session oldSession = chatRoom2SessionsMap.get(roomId).get(session.getId());
            if (oldSession != null && oldSession.isOpen()) {

                oldSession.close();

            }

        }

        chatRoom2SessionsMap.get(roomId).put(session.getId(), session);

        return true;

    }


    @Override
    public boolean sendGroupChatMsg(@Nonnull Long roomId, @Nonnull User user, @Nonnull GroupChatMessage groupChatMessage) throws IOException {

        String msg = groupChatMessage.msg();

        if (chatRoom2SessionsMap.containsKey(roomId) && StringUtils.hasLength(msg)) {

            Map<String, Session> sessionsMap = chatRoom2SessionsMap.get(roomId);

            if (sessionsMap != null && !sessionsMap.isEmpty()) {

                for (Map.Entry<String, Session> entry : sessionsMap.entrySet()) {

                    entry.getValue().getBasicRemote().sendText(String.format("[%s] : %s", groupChatMessage.isSystem() ? "系统消息" : "用户消息", msg));

                }

                return true;

            }

        }

        return false;

    }


    @Override
    public void removeSession(@Nonnull Long roomId, @Nonnull Session session) throws IOException {

        if (session.isOpen()) {

            session.close();

        }
        Map<String, Session> sessionsMap = chatRoom2SessionsMap.get(roomId);
        chatRoom2SessionsMap.get(roomId).remove(session.getId());

    }


}