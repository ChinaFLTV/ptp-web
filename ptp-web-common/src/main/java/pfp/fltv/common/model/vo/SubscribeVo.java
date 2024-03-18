package pfp.fltv.common.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/18 下午 9:36:55
 * @description 前端发送过来的用户订阅消息
 * @filename SubscribeVo.java
 */

@Schema(description = "前端发送过来的用户订阅消息")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscribeVo implements Serializable {


    @Schema(description = "订阅发起者ID")
    private Long followerId;

    @Schema(description = "被订阅者ID")
    private Long subscriberId;

    @Schema(description = "订阅坐标", example = "passage.java.design_mode")
    private String coordinate;


}
