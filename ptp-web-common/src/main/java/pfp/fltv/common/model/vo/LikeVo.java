package pfp.fltv.common.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    // TODO 该字段类型日后有可能改为枚举常量
    @Schema(description = "动作类型", examples = {"like", "unlike"})
    private String type;

    // TODO 该字段类型日后有可能改为枚举常量
    @Schema(description = "动作所在的作用域", examples = {"passage", "dialogue", "passage_comment"})
    private String scope;

    @Schema(description = "目标对象ID")
    private Long toId;


}
