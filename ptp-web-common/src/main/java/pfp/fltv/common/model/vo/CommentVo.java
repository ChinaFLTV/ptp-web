package pfp.fltv.common.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import pfp.fltv.common.model.po.content.Comment;
import pfp.fltv.common.model.po.info.AddressInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/18 下午 9:32:56
 * @description 前端传过来的内容评论信息
 * @filename CommentVo.java
 */

@Schema(description = "前端传过来的内容评论信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentVo implements Serializable {


    @Schema(description = "评论ID")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @Field(name = "content_id", type = FieldType.Constant_Keyword)
    @Schema(description = "评论的内容的ID")
    private Long contentId;

    @Field(name = "belong_type", type = FieldType.Constant_Keyword)
    @Schema(description = "评论所附属的内容类型")
    private Comment.BelongType belongType;

    @Schema(description = "评论所属用户(发布者)ID")
    private Long fromUid;

    @Field(name = "from_nickname", type = FieldType.Constant_Keyword)
    @Schema(description = "评论所属用户(发布者)昵称")
    private String fromNickname;

    @Field(name = "from_avatar_url", type = FieldType.Constant_Keyword)
    @Schema(description = "评论所属用户(发布者)头像URL")
    private String fromAvatarUrl;

    @Schema(description = "回复的用户ID(如果是文章的一级评论，则此值为null)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long toUid;

    @Field(name = "to_nickname", type = FieldType.Constant_Keyword)
    @Schema(description = "回复的用户昵称")
    private String toNickname; // 2024-10-16  00:44-新增 回复的用户昵称 属性

    @Field(name = "to_avatar_url", type = FieldType.Constant_Keyword)
    @Schema(description = "回复的用户头像URL")
    private String toAvatarUrl; // 2024-10-16  00:44-新增 回复的用户头像URL 属性

    @Schema(description = "父评论ID(如果有的话),因为可能存在这样的评论：在一条已评论了文章的评论下，回复该评论收到的其他回复", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long parentId;

    @Schema(description = "所属主题ID(用于根据主题进行分库分表以减缓数据库压力),该ID的生成将由其他服务根据文章的分类和标签动态生成(一般是约定好了的)")
    private Long topicId;

    @Schema(description = "内容", minLength = 1, maxLength = 255)
    private String content;

    @Schema(description = "附加的其他类型的媒体内容(JSON格式)")
    private String accessary;

    @Schema(description = "标签")
    private List<String> tags;

    @Schema(description = "分类")
    private List<String> category;

    @Schema(description = "记录文章评论发布时的地址信息")
    private AddressInfo addressInfo;


}
