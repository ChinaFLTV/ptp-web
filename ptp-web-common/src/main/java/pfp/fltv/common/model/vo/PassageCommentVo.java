package pfp.fltv.common.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/18 下午 9:32:56
 * @description 前端传过来的文章评论信息
 * @filename PassageCommentVo.java
 */

@Schema(description = "前端传过来的文章评论信息")
@Data
@NoArgsConstructor
//@AllArgsConstructor
@Builder
public class PassageCommentVo implements Serializable {


}
