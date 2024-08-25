package pfp.fltv.common.model.po.ws;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/24 PM 11:45:52
 * @description WebSocket中的群聊消息
 * @filename GroupChatMessage.java
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupMessage {


    private Long id; // 2024-8-23:58-消息ID
    private String content; //  2024-8-23  11:59-消息内容
    private Long senderId; // 2024-8-23  1:59-发送者ID
    private String senderNickname; // 2024-8-23  11:59-发送者昵称
    private String senderAvatarUrl; // 2024-8-23  12:00-发送者头像URL
    private Long receiverId = -1L; // 2024-8-23  12:00-接受者ID(-1则为全部聊天成员接收)
    private Long chatRoomId; // 2024-8-23  13:20-如果是群聊消息 , 则该字段有效
    private LocalDateTime dateTime = LocalDateTime.now(); // 2024-6-25  11:02-消息产生时间
    private MessageType type = MessageType.UNKNOWN; // 2024-8-23  12:01-消息类型


    @Getter
    @AllArgsConstructor
    public enum MessageType {


        GROUP_CHAT(1701, "群聊消息"),
        PRIVATE_CHAT(1702, "私聊消息"),
        SYSTEM(1703, "系统消息"),
        UNKNOWN(1704, "未知类型的消息");


        @JsonValue
        private final Integer code;
        private final String comment;


    }


}