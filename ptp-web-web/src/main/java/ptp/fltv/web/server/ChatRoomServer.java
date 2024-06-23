package ptp.fltv.web.server;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson2.JSON;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import pfp.fltv.common.annotation.LogRecord;
import ptp.fltv.web.service.ChatRoomService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/23 PM 10:25:02
 * @description 聊天室 社交功能相关的服务端
 * @filename ChatRoomServer.java
 */

@AllArgsConstructor
@Tag(name = "聊天室服务端点")
@RestController
@ServerEndpoint("/content/user/chat/room/{roomId}")
public class ChatRoomServer {


    private ChatRoomService chatRoomService;


    @LogRecord(description = "开启一个聊天室的新会话")
    @SentinelResource("web-content-user-char-room-controller")
    @OnOpen
    public void openSession(@PathParam("roomId") Long roomId, Session session) throws IOException {

        boolean isJoinSuccessfully = chatRoomService.addSessionToChatRoom(roomId, session);
        if (isJoinSuccessfully) {

            Map<String, Object> customMsg = new HashMap<>();
            customMsg.put("roomId", roomId);
            customMsg.put("userId", -1);
            customMsg.put("msg", "欢迎用户ID为 " + -1 + " 加入到当前聊天室里来!");
            chatRoomService.sendGroupChatMsg(JSON.toJSONString(customMsg));

        }

    }


    @LogRecord(description = "聊天室的某个会话被正常关闭")
    @OnClose
    public void onSessionClose(Session session) {


    }


    @LogRecord(description = "接收到聊天室的某个会话发来的消息")
    @SentinelResource("web-content-user-char-room-controller")
    @OnMessage
    public void onReceiveMessage(String msg) {


    }


    @LogRecord(description = "当前聊天室中的某个会话出现异常")
    @OnError
    public void onSessionError(Throwable ex, Session session) {

        ex.printStackTrace();

    }


}
