package pfp.fltv.common.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/16 上午 10:54:39
 * @description 消息时间的实体类(PO)
 * @filename MessageEvent.java
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageEvent {


    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "事件ID")
    private Long id;
    @Schema(description = "事件标题", minLength = 8, maxLength = 256)
    private String title;
    @Schema(description = "事件说明", minLength = 16, maxLength = 1024)
    private String content;
    @TableLogic
    @Schema(description = "事件是否已被逻辑移除")
    private Integer is_deleted;


}
