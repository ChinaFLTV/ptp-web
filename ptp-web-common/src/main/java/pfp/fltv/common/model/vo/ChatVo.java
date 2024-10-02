package pfp.fltv.common.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfp.fltv.common.model.po.ws.ChatRoom;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/10/2 PM 9:10:53
 * @description 用户消息中的单个聊天的数据VO实体(前后端通用)
 * @filename ChatVo.java
 */

@Schema(description = "用户消息中的单个聊天的数据VO实体(前后端通用)")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatVo {


    /*@Schema(description = "ID")
    private Long id;*/

    @Schema(description = "如果是私聊的话 , 该字段不为空 , 其值为对方用户的ID")
    private Long uid;

    @Schema(description = "如果是群聊的话 , 该字段不为空 , 其值为群组的ID")
    private Long rid;

    @Schema(description = "聊天中的对方昵称 , 如果是群聊的话 , 则该值为群聊名称")
    private String name;

    @Schema(description = "群聊或私聊中聊天对方的头像URL")
    private String avatarUrl;

    @Schema(description = "群聊/私聊中最新一条消息的发布者ID")
    private Long latestMsgSendUserId;

    @Schema(description = "群聊/私聊中最新一条消息的发布者的昵称")
    private String latestMsgSendUserNickname;

    @Schema(description = "群聊/私聊中最新一条消息的发布者的头像URL")
    private String latestMsgSendUserAvatarUrl;

    @Schema(description = "群聊/私聊中最新一条消息的发布时间")
    private LocalDateTime latestMsgPubdate;

    @Schema(description = "群聊/私聊中最新一条消息的内容")
    private String latestMsgContent;

    @Schema(description = "群聊/私聊的特色标签")
    private List<String> tags;

    @Schema(description = "内容创建时间")
    private LocalDateTime createTime;

    @Schema(description = "内容修改时间")
    private LocalDateTime updateTime;


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/10/2 PM 10:44:26
     * @version 1.0.0
     * @description 从ChatRoom数据实体中提取出有用的信息并填充进新的ChatVo实体中
     * @filename ChatVo.java
     */
    public static ChatVo fromChatRoom(@Nonnull ChatRoom chatRoom) {

        ChatVo chatVo = new ChatVo();

        chatVo.setRid(chatRoom.getId());
        chatVo.setName(chatRoom.getName());
        chatVo.setAvatarUrl(chatRoom.getAvatarUrl());

        return chatVo;

    }


}