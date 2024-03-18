package pfp.fltv.common.model.base.content;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfp.fltv.common.model.po.info.AddressInfo;

import java.io.Serializable;
import java.sql.Timestamp;

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
@Builder
public class BaseEntity implements Serializable {


    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "发布者ID")
    private Long uid;

    @Schema(description = "发布时间")
    private Timestamp createTime;

    @Schema(description = "(最后)更新时间")
    private Timestamp updateTime;

    @Schema(description = "标题", minLength = 2, maxLength = 128)
    private String title;

    @Schema(description = "内容", minLength = 8, maxLength = 1024)
    private String content;

    @Schema(description = "附加的其他类型的媒体内容(JSON格式)")
    private String accessary;

    @Schema(description = "标签")
    private String tags;

    @Schema(description = "分类")
    private String category;

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

    @Schema(description = "发布时用户所在的地址信息")
    private AddressInfo addressInfo;

    @Schema(description = "实例状态")
    private Integer status;

    @Schema(description = "其他数据配置(JSON)")
    private String meta;

    @Schema(description = "当前实体是否已被逻辑删除")
    @TableLogic
    private Integer isDeleted;


}
