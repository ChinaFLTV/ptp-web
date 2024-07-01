package pfp.fltv.common.model.po.content;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Setting;
import pfp.fltv.common.model.base.content.BaseEntity;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/14 下午 9:18:30
 * @description 对话(PO实体类)
 * @filename Dialogue.java
 */

@Setting(sortOrders = Setting.SortOrder.desc)
@Document(indexName = "dialogue")
@TableName(value = "dialogue", autoResultMap = true)
@Schema(description = "对话(PO实体类)")
public class Dialogue extends BaseEntity {


}
