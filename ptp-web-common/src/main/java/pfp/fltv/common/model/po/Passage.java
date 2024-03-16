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
 * @date 2024/3/14 下午 4:03:20
 * @description 文章的实体类(PO)
 * @filename Passage.java
 */

@TableName("passage")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Passage implements Serializable {


    @TableId(type = IdType.ASSIGN_ID)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "文章ID")
    private Long passageId;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "发布者ID")
    private Long uid;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "发布时间")
    private Timestamp pubdate;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "标题")
    private String title;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "引子")
    private String intro;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "内容")
    private String content;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "附件")
    private String accessary;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "分类")
    private String category;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "浏览量")
    private Integer browseNum;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "点赞量")
    private Integer likeNum;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "收藏量")
    private Integer collectNum;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "原始数据类型")
    private String fileType;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "发布时所在的IP属地")
    private String location;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "当前状态")
    private Integer status;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "其他数据配置")
    private String config;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "文章是否已被逻辑删除")
    @TableLogic
    private Integer is_deleted;


}
