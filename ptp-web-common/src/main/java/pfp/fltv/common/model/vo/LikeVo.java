package pfp.fltv.common.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfp.fltv.common.enums.Action;
import pfp.fltv.common.enums.Scope;

import java.io.Serializable;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/18 下午 10:30:32
 * @description 前端传过来的用户点赞/取消点赞的信息
 * @filename LikeVo.java
 */

@Schema(description = "前端传过来的用户点赞/取消点赞的信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeVo implements Serializable {


    @Schema(description = "动作发起者ID")
    private Long uid;

    @Schema(description = "动作类型", examples = {"like", "unlike"})
    private Action type;

    @Schema(description = "动作所在的作用域", examples = {"passage", "dialogue", "passage_comment"})
    private Scope scope;

    @Schema(description = "目标对象ID")
    private Long toId;


}
