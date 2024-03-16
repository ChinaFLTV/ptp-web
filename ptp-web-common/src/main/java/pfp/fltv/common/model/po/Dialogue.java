package pfp.fltv.common.model.po;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/14 下午 9:18:30
 * @description
 * @filename Dialogue.java
 */

@TableName("dialogue")
@Schema(description = "对话(PO实体类)")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dialogue implements Serializable {


    @TableId(type = IdType.ASSIGN_ID)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "言论ID")
    private Long dialogueId;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "对话所属对象")
    private Long uid;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "对话上传时间")
    private Timestamp pubdate;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "对话主体内容")
    private String content;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "附加的其他类型的媒体内容")
    private String accessary;

    @TableField("accessary_type")
    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "附加的媒体内容类型(0为没有附加媒体内容，1为图片，2为视频，3为音频)")
    private Integer accessaryType = 0;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "点赞量")
    private Integer likeNum = 0;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "评论量")
    private Integer commentNum = 0;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "浏览量")
    private Integer browseNum = 0;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "当前言论状态")
    private Integer status;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "言论发布时所在的IP属地")
    private String location;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "其他附加数据配置")
    private String config;

    @TableLogic
    private Integer isDeleted;


}
