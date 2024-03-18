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
 * @date 2024/3/18 下午 10:11:34
 * @description 点赞/取消点赞相关的事件实体类
 * @filename LikeEvent.java
 */

@Schema(description = "点赞/取消点赞相关的事件实体类")
@TableName("like_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeEvent extends BaseEntity {


    // TODO 该字段类型日后有可能改为枚举常量
    @Schema(description = "动作类型", examples = {"like", "unlike"})
    private String type;

    // TODO 该字段类型日后有可能改为枚举常量
    @Schema(description = "动作所在的作用域", examples = {"passage", "dialogue", "passage_comment"})
    private String scope;

    @Schema(description = "目标对象ID")
    private Long toId;


}
