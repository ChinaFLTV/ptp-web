package pfp.fltv.common.model.po.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pfp.fltv.common.enums.base.ConvertableEnum;
import pfp.fltv.common.model.base.system.BaseEntity;
import pfp.fltv.common.model.po.content.Comment;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/10/29 AM 1:11:18
 * @description 点赞/收藏/分享等事件以及对应的取消事件的事件记录数据PO实体
 * @filename EventRecord.java
 */

@TableName(value = "event_record", autoResultMap = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EventRecord extends BaseEntity {


    @Schema(description = "记录产生者(发布者)昵称")
    private String nickname;

    @Schema(description = "记录产生者(发布者)头像URL")
    private String avatarUrl;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(description = "事件的目标内容实体的类型")
    private Comment.BelongType contentType;

    @Schema(description = "事件的目标内容实体的ID")
    private Long contentId;

    @Schema(description = "内容实体标题(如果有的话)")
    private String contentTitle; // 2024-10-29  1:36-内容实体标题(如果有的话)

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(description = "事件类型")
    private EventType eventType;

    @Schema(description = "事件备注")
    private String remark;


    /**
     * @author Lenovo/LiGuanda
     * @version 1.0.0
     * @date 2024/10/29 AM 1:37:55
     * @description 事件类型的枚举类
     * @filename EventRecord.java
     */
    @Getter
    @AllArgsConstructor
    public enum EventType implements ConvertableEnum<Integer> {


        LIKE(2401, "点赞"),
        CANCEL_LIKE(2402, "取消点赞"),
        STAR(2403, "收藏"),
        CANCEL_STAR(2404, "取消收藏"),
        SHARE(2405, "分享"),
        CANCEL_SHARE(2406, "取消分享"),
        COMMENT(2407, "评论"),
        CANCEL_COMMENT(2408, "取消评论"),
        SUBSCRIBE(2407, "关注"),
        CANCEL_SUBSCRIBE(2408, "取消关注"),
        REPLY(2409, "回复"),
        CANCEL_REPLY(2410, "取消回复"),
        RATE(2411, "评分"),
        CANCEL_RATE(2412, "取消评分"),
        REPORT(2413, "举报"),
        CANCEL_REPORT(2414, "取消举报"),
        EDIT(2415, "编辑"),
        CANCEL_EDIT(2416, "取消编辑"),
        PUBLISH(2417, "发布"),
        CANCEL_PUBLISH(2418, "取消发布"),
        SUBMIT(2419, "提交"),
        CANCEL_SUBMIT(2420, "取消提交"),
        UPLOAD(2421, "上传"),
        CANCEL_UPLOAD(2422, "取消上传"),
        DOWNLOAD(2423, "下载"),
        CANCEL_DOWNLOAD(2424, "取消下载"),
        UNLIKE(2425, "倒赞"),
        CANCEL_UNLIKE(2426, "取消倒赞"),
        BROWSE(2427, "浏览"),
        CANCEL_BROWSE(2428, "取消浏览"),
        FOLLOWED(2429, "被关注"),
        CANCEL_FOLLOWED(2430, "取消被关注");


        @JsonValue
        private final Integer code;
        private final String comment;


    }


}