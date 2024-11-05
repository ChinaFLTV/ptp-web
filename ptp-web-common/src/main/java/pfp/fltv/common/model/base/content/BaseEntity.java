package pfp.fltv.common.model.base.content;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import pfp.fltv.common.enums.ContentFormat;
import pfp.fltv.common.enums.ContentStatus;
import pfp.fltv.common.enums.base.ConvertableEnum;
import pfp.fltv.common.model.po.info.AddressInfo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/17 下午 8:58:11
 * @description 言论、公告、文章、评论等实体类的基类，以提升代码复用性
 * @filename BaseEntity.java
 */

@Schema(description = "实体类的公共基类，用于聚合属性提升代码复用性")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@SuperBuilder
public class BaseEntity implements Serializable {


    @Id
    @Field(type = FieldType.Constant_Keyword)
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "ID")
    private Long id;

    @Field(type = FieldType.Constant_Keyword)
    @Schema(description = "发布者ID")
    private Long uid;

    @Field(type = FieldType.Text, analyzer = "analysis-ik")
    @Schema(description = "标题", minLength = 2, maxLength = 128)
    private String title;

    @Transient
    @Schema(description = "内容", minLength = 8, maxLength = 1024)
    private String content;

    @Field(type = FieldType.Keyword)
    @Schema(description = "内容形式")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ContentFormat contentFormat;

    @Transient
    @Schema(description = "附加的其他类型的媒体内容(JSON格式)")
    private String accessary;

    @Transient
    @Schema(description = "标签")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> tags = new ArrayList<>();

    @Transient
    @Schema(description = "分类")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> category = new ArrayList<>();

    @Field(name = "browse_num", type = FieldType.Integer)
    @Schema(description = "浏览量")
    private Integer browseNum = 0;

    @Field(name = "like_num", type = FieldType.Integer)
    @Schema(description = "点赞量")
    private Integer likeNum = 0;

    @Field(name = "unlike_num", type = FieldType.Integer)
    @Schema(description = "倒赞量")
    private Integer unlikeNum = 0;

    @Field(name = "comment_num", type = FieldType.Integer)
    @Schema(description = "评论量")
    private Integer commentNum = 0;

    @Field(name = "star_num", type = FieldType.Integer)
    @Schema(description = "收藏量")
    private Integer starNum = 0;

    @Field(name = "share_num", type = FieldType.Integer)
    @Schema(description = "转发量")
    private Integer shareNum = 0; // 2024-10-12  20:18-增加转发量字段

    @Transient
    @Schema(description = "发布时用户所在的地址信息")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private AddressInfo addressInfo;

    @Field(type = FieldType.Keyword)
    @Schema(description = "实例状态")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ContentStatus status = ContentStatus.NORMAL;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Transient
    @Schema(description = "其他数据配置(JSON)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Map<String, Object> meta;

    // 2024-6-14  11:32-统一将数据类型由TimeStamp调整为LocalDateTime，因为LocalDateTime类型也可以对应SQL中的TImeStamp类型，并且LocalDateTime操作起来更方便
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

    @Transient
    @Schema(description = "当前实体的版本(用于辅助实现乐观锁)")
    @Version
    private Integer version = 1;


    /**
     * @author Lenovo/LiGuanda
     * @version 1.0.0
     * @date 2024/6/19 PM 9:59:17
     * @description 内容实体的类型枚举常量
     * @filename BaseEntity.java
     */
    @AllArgsConstructor
    @Getter
    public enum ContentType implements Serializable, ConvertableEnum<Integer> {


        ANNOUNCEMENT(1501, "announcement", "公告"),
        DIALOGUE(102, "dialogue", "对话"),
        PASSAGE(1503, "passage", "文章"),
        PASSAGE_COMMENT(1504, "passage:comment", "文章评论"),
        UNKNOWN(1505, "unknown", "未知内容");


        @JsonValue
        private final Integer code;
        private final String subkey; // 2024-6-21  11:25-用于合成Redis的key，主要解决passage:comment这种特殊的构成
        private final String comment;


        /**
         * @param code 需要被转换的code值
         * @return 转换后的对应的ContentType类型的枚举常量
         * @author Lenovo/LiGuanda
         * @date 2024/6/19 PM 10:07:11
         * @version 1.0.0
         * @description 将指定整形类型的code值转换为对应的ContentType枚举常量
         * @filename BaseEntity.java
         */
        public static ContentType valueOfByCode(@Nonnull Integer code) {

            for (ContentType contentType : values()) {

                if (Objects.equals(contentType.code, code)) {

                    return contentType;

                }

            }

            return UNKNOWN;

        }


    }


}
