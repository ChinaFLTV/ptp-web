package pfp.fltv.common.model.vo;

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
 * @date 2024/3/14 下午 3:56:53
 * @description 公告的展示类(VO)
 * @filename AnnouncementVo.java
 */


@Schema(description = "用于展示/数据传输的公告(VO与DTO融合版)")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnouncementVo implements Serializable {


    @Schema(description = "公告ID")
    private Long id;

    @Schema(description = "发布者ID")
    private Long uid;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户填写的公告标题")
    private String title;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户填写的公告内容")
    private String content;

    @Schema(description = "附加的其他类型的媒体内容(JSON格式)")
    private String accessary;

    @Schema(description = "标签")
    private List<String> tags;

    @Schema(description = "分类")
    private List<String> category;

    @Schema(description = "发布时用户所在的地址信息")
    private AddressInfo addressInfo;


}
