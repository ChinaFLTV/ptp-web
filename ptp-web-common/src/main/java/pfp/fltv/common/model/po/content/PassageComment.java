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
import pfp.fltv.common.model.po.info.AddressInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/14 下午 9:31:59
 * @description 文章评论的实体类(PO)
 * @filename PassageComment.java
 */

@TableName(value = "passage_comment", autoResultMap = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassageComment implements Serializable {


    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "评论的文章ID")
    private Long passageId;

    @Schema(description = "评论所属用户(发布者)ID")
    private Long fromUid;

    @Schema(description = "回复的用户ID(如果是文章的一级评论，则此值为null)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long toUid;

    @Schema(description = "父评论ID(如果有的话),因为可能存在这样的评论：在一条已评论了文章的评论下，回复该评论收到的其他回复", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long parentUid;

    @Schema(description = "所属主题ID(用于根据主题进行分库分表以减缓数据库压力),该ID的生成将由其他服务根据文章的分类和标签动态生成(一般是约定好了的)")
    private Long topicId;

    @Schema(description = "内容", minLength = 1, maxLength = 255)
    private String content;

    @Schema(description = "标签")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> tags;

    @Schema(description = "分类")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> category;

    @Schema(description = "浏览量")
    private Integer browseNum;

    @Schema(description = "点赞量")
    private Integer likeNum;

    @Schema(description = "倒赞量")
    private Integer unlikeNum;

    @Schema(description = "评论量")
    private Integer commentNum;

    @Schema(description = "收藏量")
    private Integer starNum;

    @Schema(description = "记录文章评论发布时的地址信息")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private AddressInfo addressInfo;


}