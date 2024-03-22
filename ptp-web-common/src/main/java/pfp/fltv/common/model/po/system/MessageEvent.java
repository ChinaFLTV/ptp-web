package pfp.fltv.common.model.po.system;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pfp.fltv.common.model.base.system.BaseEntity;

import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/16 上午 10:54:39
 * @description 一般事件流实体类(PO)
 * @filename MessageEvent.java
 */

@Schema(description = "一般事件流实体类")
@TableName("message_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MessageEvent extends BaseEntity {


    @Schema(description = "事件标题", minLength = 8, maxLength = 256)
    private String title;
    @Schema(description = "事件说明", minLength = 16, maxLength = 1024)
    private String content;
    /* @Schema(description = "流水参与者ID")
     private Long participantId;*/
    @Schema(description = "备注信息")
    private Map<String, String> remarks;


}
