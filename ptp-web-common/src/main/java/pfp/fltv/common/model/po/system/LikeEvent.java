package pfp.fltv.common.model.po.system;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pfp.fltv.common.enums.Action;
import pfp.fltv.common.enums.Scope;
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
@SuperBuilder
public class LikeEvent extends BaseEntity {


    @Schema(description = "动作类型", examples = {"like", "unlike"})
    private Action type;

    @Schema(description = "动作所在的作用域", examples = {"passage", "dialogue", "passage_comment"})
    private Scope scope;

    @Schema(description = "目标对象ID")
    private Long toId;


}
