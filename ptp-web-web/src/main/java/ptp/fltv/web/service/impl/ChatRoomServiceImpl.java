package ptp.fltv.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Nonnull;
import jakarta.websocket.Session;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ptp.fltv.web.service.ChatRoomService;

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

@AllArgsConstructor
@Service
public class ChatRoomServiceImpl implements ChatRoomService {


    private final Map<Long, Map<String, Session>> chatRoom2SessionsMap = new HashMap<>(); // 2024-6-23  23:46-存储房间号以及其当前持有的SESSION的映射


    @Override
    public boolean addSessionToChatRoom(@Nonnull Long roomId, @Nonnull Session session) {

        if (!chatRoom2SessionsMap.containsKey(roomId)) {

            chatRoom2SessionsMap.put(roomId, new HashMap<>());

        }
        chatRoom2SessionsMap.get(roomId).put(session.getId(), session);

        return true;

    }


    @Override
    public boolean sendGroupChatMsg(@Nonnull String customMsgBody) throws IOException {

        JSONObject customMsg = JSON.parseObject(customMsgBody);
        if (customMsg != null) {

            Long roomId = customMsg.getLong("roomId");
            Long userId = customMsg.getLong("userId");
            String msg = customMsg.getString("msg");

            if (roomId != null && chatRoom2SessionsMap.containsKey(roomId) &&
                    userId != null && StringUtils.hasLength(msg)) {

                Map<String, Session> sessionsMap = chatRoom2SessionsMap.get(roomId);

                if (sessionsMap != null && !sessionsMap.isEmpty()) {

                    for (Map.Entry<String, Session> entry : sessionsMap.entrySet()) {

                        entry.getValue().getBasicRemote().sendText(msg);

                    }

                    return true;

                }

            }

        }

        return false;

    }


}