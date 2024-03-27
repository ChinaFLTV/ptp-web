package pfp.fltv.common.model.po.content;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import pfp.fltv.common.model.base.content.BaseEntity;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/14 下午 9:18:30
 * @description
 * @filename Dialogue.java
 */

@TableName(value = "dialogue", autoResultMap = true)
@Schema(description = "对话(PO实体类)")
public class Dialogue extends BaseEntity {


}
