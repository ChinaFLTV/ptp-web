package pfp.fltv.common.model.po.ws;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import pfp.fltv.common.enums.base.ConvertableEnum;

import java.time.LocalDateTime;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/24 PM 11:45:52
 * @description WebSocket中的群聊消息
 * @filename GroupChatMessage.java
 */

@Document(collection = "GroupMessage") // 2024-9-10  21:45-指定该实体在MongoDB存放的表的表明 , 若不设置 , 则默认为类名开头小写的名称
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupMessage {


    @Id
    private Long id; // 2024-8-23:58-消息ID
    private String content; //  2024-8-23  11:59-消息内容
    // 2024-9-11  21:35-将background值设置为true , 使得索引创建的过程将异步执行 , 因为索引创建过程比较耗费时间 , 因此异步执行可有效避免索引的创建导致主线程被阻塞的风险
    @Indexed(background = true)
    private Long senderId; // 2024-8-23  1:59-发送者ID
    private String senderNickname; // 2024-8-23  11:59-发送者昵称
    private String senderAvatarUrl; // 2024-8-23  12:00-发送者头像URL
    private Long receiverId = -1L; // 2024-8-23  12:00-接受者ID(-1则为全部聊天成员接收)
    @Indexed(background = true)
    private Long chatRoomId; // 2024-8-23  13:20-如果是群聊消息 , 则该字段有效
    @Indexed(background = true)
    private LocalDateTime dateTime = LocalDateTime.now(); // 2024-6-25  11:02-消息产生时间
    private String dataUri; // 2024-8-30  22:58-如果内容类型是多媒体(非纯文本)数据类型 , 则该字段存储对应数据的直链
    private ContentType contentType = ContentType.TEXT; // 2024-8-30  22:42-消息内容类型
    private MessageType messageType = MessageType.UNKNOWN; // 2024-8-23  12:01-消息类型


    @Getter
    @AllArgsConstructor
    public enum MessageType implements ConvertableEnum<Integer> {


        GROUP_CHAT(1701, "群聊消息"),
        PRIVATE_CHAT(1702, "私聊消息"),
        SYSTEM_USER_ENTER(1703, "系统消息:用户进入房间"),
        SYSTEM_USER_EXIT(1704, "系统消息:用户退出房间"),
        SYSTEM_ABNORMAL(1705, "系统消息:系统聊天服务出现异常"),
        SYSTEM_RECOVER(1706, "系统消息:系统聊天服务恢复正常"), // 2024-8-26  14:06-继续增加新的系统消息时 , 请保持系统消息枚举的连续性 , 同时你还需要去ptp.fltv.web.service.impl.ChatRoomServiceImpl.sendPrivateChatMsg方法中修改对系统消息的上下限检测的代码(前端的代码也要同步进行修改)
        UNKNOWN(1707, "未知类型的消息");


        @JsonValue
        private final Integer code;
        private final String comment;


    }


    /**
     * @author Lenovo/LiGuanda
     * @version 1.0.0
     * @date 2024/8/30 PM 10:32:40
     * @description 消息内容类型枚举
     * @filename GroupMessage.java
     */
    @Getter
    @AllArgsConstructor
    public enum ContentType implements ConvertableEnum<Integer> {


        TEXT(1801, "纯文本"),
        PHOTO(1802, "图片"),
        VIDEO(1803, "视频"),
        AUDIO(1804, "音频"),
        VOICE(1805, "语音"),
        FILE(1806, "文件");


        @JsonValue
        private final Integer code;
        private final String comment;


    }


}