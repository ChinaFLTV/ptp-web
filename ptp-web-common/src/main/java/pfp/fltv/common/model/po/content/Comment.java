package pfp.fltv.common.model.po.content;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;
import pfp.fltv.common.enums.ContentFormat;
import pfp.fltv.common.enums.base.ConvertableEnum;
import pfp.fltv.common.model.po.info.AddressInfo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/14 下午 9:31:59
 * @description 言论/文章等内容的评论的实体类(PO)
 * @filename Comment.java
 */

@Setting(sortOrders = Setting.SortOrder.desc)
@Document(indexName = "comment")
@TableName(value = "comment", autoResultMap = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment implements Serializable {


    @Id
    @Field(type = FieldType.Constant_Keyword)
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "ID")
    private Long id;

    @Field(name = "content_id", type = FieldType.Constant_Keyword)
    @Schema(description = "评论的内容的ID")
    private Long contentId;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Field(name = "belong_type", type = FieldType.Constant_Keyword)
    @Schema(description = "评论所附属的内容类型")
    private BelongType belongType;

    @Field(name = "from_uid", type = FieldType.Constant_Keyword)
    @Schema(description = "评论所属用户(发布者)ID")
    private Long fromUid;

    @Field(name = "from_nickname", type = FieldType.Constant_Keyword)
    @Schema(description = "评论所属用户(发布者)昵称")
    private String fromNickname;

    @Field(name = "from_avatar_url", type = FieldType.Constant_Keyword)
    @Schema(description = "评论所属用户(发布者)头像URL")
    private String fromAvatarUrl;

    @Field(name = "to_uid", type = FieldType.Constant_Keyword)
    @Schema(description = "回复的用户ID(如果是文章的一级评论，则此值为null)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long toUid;

    @Field(name = "to_nickname", type = FieldType.Constant_Keyword)
    @Schema(description = "回复的用户昵称")
    private String toNickname; // 2024-10-16  00:43-新增 回复的用户昵称 属性

    @Field(name = "to_avatar_url", type = FieldType.Constant_Keyword)
    @Schema(description = "回复的用户头像URL")
    private String toAvatarUrl; // 2024-10-16  00:44-新增 回复的用户头像URL 属性

    @Field(name = "parent_uid", type = FieldType.Constant_Keyword)
    @Schema(description = "父评论ID(如果有的话),因为可能存在这样的评论：在一条已评论了文章的评论下，回复该评论收到的其他回复", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long parentId;

    @Field(name = "topic_id", type = FieldType.Constant_Keyword)
    @Schema(description = "所属主题ID(用于根据主题进行分库分表以减缓数据库压力),该ID的生成将由其他服务根据文章的分类和标签动态生成(一般是约定好了的)")
    private Long topicId;

    @Field(type = FieldType.Text, analyzer = "analysis-ik")
    @Schema(description = "内容", minLength = 1, maxLength = 255)
    private String content;

    @Field(type = FieldType.Keyword)
    @Schema(description = "内容形式")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ContentFormat contentFormat;

    @Transient
    @Schema(description = "标签")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> tags;

    @Transient
    @Schema(description = "分类")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> category;

    @Field(name = "browse_num", type = FieldType.Integer)
    @Schema(description = "浏览量")
    private Integer browseNum;

    @Field(name = "like_num", type = FieldType.Integer)
    @Schema(description = "点赞量")
    private Integer likeNum;

    @Field(name = "unlike_num", type = FieldType.Integer)
    @Schema(description = "倒赞量")
    private Integer unlikeNum;

    @Field(name = "comment_num", type = FieldType.Integer)
    @Schema(description = "评论量")
    private Integer commentNum;

    @Field(name = "star_num", type = FieldType.Integer)
    @Schema(description = "收藏量")
    private Integer starNum;

    @Field(name = "star_num", type = FieldType.Integer)
    @Schema(description = "转发量")
    private Integer shareNum;

    @Transient
    @Schema(description = "记录评论发布时的地址信息")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private AddressInfo addressInfo;

    @Field(name = "create_time", type = FieldType.Date)
    @Schema(description = "内容创建时间")
    private LocalDateTime createTime;

    @Field(name = "update_time", type = FieldType.Date)
    @Schema(description = "(最后)更新时间")
    private LocalDateTime updateTime;

    @Field(name = "is_deleted", type = FieldType.Keyword)
    @Schema(description = "当前实体是否已被逻辑删除", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @TableLogic
    private Integer isDeleted = 0;


    /**
     * @author Lenovo/LiGuanda
     * @version 1.0.0
     * @date 2024/10/12 PM 1:42:19
     * @description 评论所附属的内容类型枚举
     * @filename Comment.java
     */
    @Getter
    @AllArgsConstructor
    public enum BelongType implements ConvertableEnum<Integer> {


        DIALOGUE(2101, "言论"),
        PASSAGE(2102, "文章"),
        COMMENT(2103, "评论"),
        USER(2104, "用户"),
        ROLE(2105, "角色"),
        ASSET(2106, "财产"),
        BANNER(2107, "轮播"),
        ANNOUNCEMENT(2108, "公告"),
        COMMODITY(2109, "商品"),
        REPORT(2110, "举报"),
        RATE(2111, "评分"),
        GROUP_MESSAGE(2112, "群聊消息"),
        PRIVATE_MESSAGE(2113, "私聊消息"),
        SYSTEM_MESSAGE(2114, "系统消息"),
        LIKE_RECORD(2115, "点赞/取消点赞记录"),
        STAR_RECORD(2116, "收藏/取消收藏记录"),
        SHARED_RECORD(2116, "收藏/取消收藏记录"),
        UPDATE_INFO(2117, "更新信息"),
        ALL(2118, "不区分类型");


        @EnumValue // 2024-10-13  21:33-解决 java.lang.IllegalArgumentException: No enum constant pfp.fltv.common.model.po.content.Comment.BelongType.XXXX 的问题
        @JsonValue
        private final Integer code;
        private final String comment;


    }


}