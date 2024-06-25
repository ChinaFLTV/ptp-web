package pfp.fltv.common.model.po.ws;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class GroupChatMessage {


    private boolean isSystem; // 2024-6-25  11:02-是否是系统消息
    private Long fromUserId; // 2024-6-25  20:56-消息发送方用户ID
    private Long toUserId; // 2024-6-25  20:56-消息接收方用户ID
    private String msg; // 2024-6-25  11:02-真正的消息内容
    private LocalDateTime dateTime = LocalDateTime.now(); // 2024-6-25  11:02-消息产生时间


}