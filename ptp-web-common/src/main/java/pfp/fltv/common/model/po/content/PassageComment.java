package pfp.fltv.common.model.po.content;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfp.fltv.common.enums.ContentStatus;
import pfp.fltv.common.model.po.info.AddressInfo;

import java.io.Serializable;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/14 下午 9:31:59
 * @description 文章评论的实体类(PO)
 * @filename PassageComment.java
 */

@TableName("passage_comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassageComment implements Serializable {


    @Schema(description = "评论ID")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "评论的文章ID")
    private Integer passageId;

    @Schema(description = "评论所属用户(发布者)ID")
    private Long fromUid;

    @Schema(description = "回复的用户ID(如果是文章的一级评论，则此值为null)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long toUid;

    @Schema(description = "父评论ID(如果有的话),因为可能存在这样的评论：在一条已评论了文章的评论下，回复该评论收到的其他回复", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long parentUid;

    @Schema(description = "所属主题ID(用于根据主题进行分库分表以减缓数据库压力),该ID的生成将由其他服务根据文章的分类和标签动态生成(一般是约定好了的)")
    private Long topicId;

    @Schema(description = "浏览量")
    private Integer browseNum;

    @Schema(description = "点赞量")
    private Integer likeNum;

    @Schema(description = "倒赞量")
    private Integer unlikeNum;

    @Schema(description = "附加的其他类型的媒体内容(JSON格式)")
    private String accessary;

    @Schema(description = "当前状态")
    private ContentStatus status;

    @Schema(description = "其他数据配置(JSON)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String meta;

    @Schema(description = "记录文章评论发布时的IP属地")
    private AddressInfo addressInfo;

    @Schema(description = "文章评论是否已被逻辑删除", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @TableLogic
    private Integer isDeleted;


}