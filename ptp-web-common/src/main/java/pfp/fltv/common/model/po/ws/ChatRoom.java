package pfp.fltv.common.model.po.ws;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/8/23 PM 12:15:39
 * @description 存储群聊房间的相关信息的数据实体
 * @filename ChatRoom.java
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {


    private Long id; // 2024-8-23  12:16-房间号
    private String name; // 2024-8-23  12:16-房间名称
    private String avatarUrl; // 2024-8-23  13:39-房间头像URL
    private Double rank; // 2024-8-23  12:17-房间等级
    private Set<Long> onlineUsers; // 2024-8-23  12:23-在线人数ID集合
    private Set<Long> totalUsers; // 2024-8-23  12:25-总共人数ID集合


}