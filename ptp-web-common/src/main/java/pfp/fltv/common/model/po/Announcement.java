package pfp.fltv.common.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/14 下午 3:26:17
 * @description 公告的实体类(PO)
 * @filename Announcement.java
 */

@TableName("announcement")
@Schema(description = "公告(PO实体类)")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Announcement implements Serializable {


    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "公告ID")
    private Long announcementId;

    @Schema(description = "发布者ID")
    private Long uid;

    @Schema(description = "发布时间")
    private Timestamp pubdate;

    @Schema(description = "标题", minLength = 2, maxLength = 128)
    private String title;

    @Schema(description = "内容", minLength = 8, maxLength = 1024)
    private String content;

    @Schema(description = "附件")
    private String accessary;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "浏览量")
    private Integer browseNum;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "点赞量")
    private Integer likeNum;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "收藏量")
    private Integer collectNum;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "评论量")
    private Integer commentNum;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "发布时用户所在的IP属地")
    private String location;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "文章当前所处状态")
    private Integer status;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "其他数据配置")
    private String config;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "公告是否已被逻辑删除")
    @TableLogic
    private Integer isDeleted;


}
