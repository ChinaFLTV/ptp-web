package pfp.fltv.common.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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


    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户填写的公告标题")
    private String title;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户填写的公告内容")
    private String content;


}
