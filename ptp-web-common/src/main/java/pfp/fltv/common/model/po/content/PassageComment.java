package pfp.fltv.common.model.po.content;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;
import pfp.fltv.common.model.po.info.AddressInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/14 下午 9:31:59
 * @description 文章评论的实体类(PO)
 * @filename PassageComment.java
 */

@Setting(sortOrders = Setting.SortOrder.desc)
@Document(indexName = "passage_comment")
@TableName(value = "passage_comment", autoResultMap = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassageComment implements Serializable {


    @Id
    @Field(type = FieldType.Long)
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "ID")
    private Long id;

    @Field(name = "passage_id", type = FieldType.Long)
    @Schema(description = "评论的文章ID")
    private Long passageId;

    @Field(name = "from_uid", type = FieldType.Long)
    @Schema(description = "评论所属用户(发布者)ID")
    private Long fromUid;

    @Field(name = "to_uid", type = FieldType.Long)
    @Schema(description = "回复的用户ID(如果是文章的一级评论，则此值为null)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long toUid;

    @Field(name = "parent_uid", type = FieldType.Long)
    @Schema(description = "父评论ID(如果有的话),因为可能存在这样的评论：在一条已评论了文章的评论下，回复该评论收到的其他回复", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long parentUid;

    @Field(name = "topic_id", type = FieldType.Long)
    @Schema(description = "所属主题ID(用于根据主题进行分库分表以减缓数据库压力),该ID的生成将由其他服务根据文章的分类和标签动态生成(一般是约定好了的)")
    private Long topicId;

    @Field(type = FieldType.Text, analyzer = "analysis-ik")
    @Schema(description = "内容", minLength = 1, maxLength = 255)
    private String content;

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

    @Transient
    @Schema(description = "记录文章评论发布时的地址信息")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private AddressInfo addressInfo;


}