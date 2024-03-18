package pfp.fltv.common.model.po.content;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
    private Long uid;

    @Schema(description = "回复的用户ID(如果是文章的一级评论，则此值为null)")
    private Long replyUid;

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

    @Schema(description = "其他数据配置(JSON)")
    private String meta;

    @Schema(description = "记录文章评论发布时的IP属地")
    private AddressInfo addressInfo;

    @Schema(description = "文章评论是否已被逻辑删除")
    @TableLogic
    private Integer isDeleted;


}