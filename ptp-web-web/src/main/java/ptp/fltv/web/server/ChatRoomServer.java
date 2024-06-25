package ptp.fltv.web.server;

import com.alibaba.fastjson2.JSON;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import pfp.fltv.common.model.po.manage.User;
import pfp.fltv.common.model.po.ws.GroupChatMessage;
import ptp.fltv.web.config.WebSocketEndpointConfig;
import ptp.fltv.web.service.ChatRoomService;
import ptp.fltv.web.service.UserService;

import java.io.IOException;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/23 PM 10:25:02
 * @description 聊天室 社交功能相关的服务端
 * @filename ChatRoomServer.java
 */

@Slf4j
// 2024-6-24  17:31-解决应用启动报 Cannot deploy POJO class [*] as it is not annotated with @ServerEndpoint 的错误 :
// Spring管理采用单例模式（singleton），而 WebSocket 是多对象的，即每个客户端对应后台的一个 WebSocket 对象，也可以理解成 new 了一个 WebSocket，这样当然是不能获得自动注入的对象了，因为这两者刚好冲突
// @Scope(proxyMode = ScopedProxyMode.NO) // 2024-6-24  19:48-禁止当前类被AOP代理以避免产生错误
// @AllArgsConstructor // 2024-6-24  21:22-不要在ServerEndPoint注解的类中通过该方式注入Bean , 因为Spring需要调用该类的无参构造函数进行AOP代理 , 请采用传统方式进行依赖注入
@Tag(name = "聊天室服务端点")
@Component
@ServerEndpoint(value = "/content/user/chat/room", configurator = WebSocketEndpointConfig.class)
public class ChatRoomServer {


    private static ChatRoomService chatRoomService;
    private static UserService userService;
    private Session session; // 2024-6-24  22:29-记录当前的会话 , 主要给onReceiveMessage方法使用
    private User user; // 2024-6-24  22:44-当前会话的用户
    private Long roomId; // 2024-6-24  22:44-当前房间号ID


    // 2024-6-24  22:23-WebSocket的Bean与其他Spring的Bean不同,对于每一次WS连接 , 它都将产生一个新的Bean实例 ,
    // 而Spring默认配置的Bean是单例的,也即仅在第一次创建Bean的时候注入一次依赖 , 而后续的新new出来的Bean实例都不再归IOC容器管理 , 也就不存在依赖注入的操作 , 因此才获取不到
    // 这里我们取了个巧 , 通过方法注入的方式注入一个类变量 , 巧妙的解决了后续不注入依赖而产生NPE的情况 , 当前你也可以通过实现ApplicationContextAware接口去解决这个问题
    @Autowired
    public void setService(ChatRoomService chatRoomService, UserService userService) {

        ChatRoomServer.chatRoomService = chatRoomService;
        ChatRoomServer.userService = userService;

    }


    // 2024-6-24  19:46-不用使用基于AOP发挥作用的注解,否则会导致 Cannot deploy POJO class [*] as it is not annotated with @ServerEndpoint 的错误
    // @LogRecord(description = "开启一个聊天室的新会话")
    // @SentinelResource("web-content-user-char-room-controller")
    @OnOpen
    public void openSession(Session session, EndpointConfig sec) throws IOException {

        this.session = session;

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();
        uriComponentsBuilder.query(session.getQueryString());
        Map<String, String> params = uriComponentsBuilder.build().getQueryParams().toSingleValueMap();

        long userId = Long.parseLong(params.get("userId"));
        user = userService.getById(userId);
        roomId = Long.parseLong(params.get("roomId"));

        boolean isJoinSuccessfully = chatRoomService.joinChatRoom(roomId, userId, session);
        GroupChatMessage groupChatMessage;

        if (isJoinSuccessfully) {

            groupChatMessage = GroupChatMessage.builder()
                    .isSystem(true)
                    .msg(String.format("欢迎用户 %s 加入到当前聊天室[%d]中来!", user.getNickname(), roomId))
                    .build();
            log.info("用户 {} (USER ID = {}) 加入到当前聊天房(ROOM ID = {})", user.getNickname(), userId, roomId);

        } else {

            groupChatMessage = GroupChatMessage.builder()
                    .isSystem(true)
                    .msg(String.format("欢迎用户 %s 加入到聊天室[%d]失败", user.getNickname(), roomId))
                    .build();
            log.warn("用户 {} (USER ID = {}) 加入到当前聊天房失败(ROOM ID = {})", user.getNickname(), userId, roomId);

        }
        boolean isInformSuccessfully = chatRoomService.sendGroupChatMsg(roomId, user, groupChatMessage);

    }


    // @LogRecord(description = "聊天室的某个会话被正常关闭")
    @OnClose
    public void onSessionClose(Session session) throws IOException {

        log.info("用户 {} (USER ID = {}) 退出了当前聊天房(ROOM ID = {})", user.getNickname(), user.getId(), roomId);
        chatRoomService.leaveChatRoom(roomId, user.getId(), this.session);

        // 2024-66-24  23:16-发送用户退出群聊的全房间广播消息
        GroupChatMessage groupChatMessage = GroupChatMessage.builder()
                .isSystem(true)
                .msg(String.format("用户 %s 已退出当前聊天室[%d]", user.getNickname(), roomId))
                .build();
        chatRoomService.sendGroupChatMsg(roomId, user, groupChatMessage);

        // 2024-6-24  22:47-加速GC , 毕竟每建立一次WS连接就要new一个ChatRoomServer实例
        this.session = null;
        this.user = null;
        this.roomId = null;

    }


    // @LogRecord(description = "接收到聊天室的某个会话发来的消息")
    // @SentinelResource("web-content-user-char-room-controller")
    @OnMessage
    public void onReceiveMessage(String msg) throws IOException {

        try {

            chatRoomService.sendGroupChatMsg(roomId, user, JSON.parseObject(msg, GroupChatMessage.class));
            log.info("用户 {} (USER ID = {}) 在聊天房(ROOM ID = {}) 发送了一条全房间广播消息 : {}", user.getNickname(), user.getId(), roomId, msg);

        } catch (Exception ex) {

            GroupChatMessage groupChatMessage = GroupChatMessage.builder()
                    .isSystem(true)
                    .msg(String.format("当前客户端[%s]本次发送消息异常 : %s", session.getId(), ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage()))
                    .build();
            boolean isSendSuccessfully = chatRoomService.sendPrivateChatMsg(roomId, user.getId(), groupChatMessage);
            // 2024-6-24  23:58-客户端封装的消息格式不规范则静默本次消息的发送 , 避免因本次操作失误而使整个会话异常断连
            log.error("用户客户端 {} (USER ID = {}) 在聊天房(ROOM ID = {}) 发送的消息在解析时出现异常 : {}", user.getNickname(), user.getId(), roomId, ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());

        }

    }


    // @LogRecord(description = "当前聊天室中的某个会话出现异常")
    @OnError
    public void onSessionError(Throwable ex, Session session) {

        log.error("用户客户端 {} (USER ID = {}) 在聊天房(ROOM ID = {}) 出现异常 : {}", user.getNickname(), user.getId(), roomId, ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());

    }


}