package pfp.fltv.common.model.po.system;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfp.fltv.common.model.base.system.BaseEntity;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/18 上午 11:12:47
 * @description 用户之间的订阅实体类(POJO)
 * @filename SubscribeEvent.java
 */

@Schema(description = "用户订阅信息")
@TableName("subscribe_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscribeEvent extends BaseEntity {


    // TODO 该字段类型日后有可能改为枚举常量
    @Schema(description = "动作类型", examples = {"subscribe", "unsubscribe"})
    private String type;

    @Schema(description = "被订阅/取关者ID")
    private Long toUid;

    @Schema(description = "订阅坐标(日后进行个性化内容推送时可能会用到)", example = "passage.java.design_mode")
    private String coordinate;


}
