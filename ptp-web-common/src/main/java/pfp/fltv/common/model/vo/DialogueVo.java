package pfp.fltv.common.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/18 下午 9:25:15
 * @description 客户端传过来的对话数据
 * @filename DialogueVo.java
 */

@Schema(description = "客户端传过来的对话数据")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DialogueVo implements Serializable {


    @Schema(description = "发布者ID")
    private Long uid;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户填写的对话内容")
    private String content;


}
