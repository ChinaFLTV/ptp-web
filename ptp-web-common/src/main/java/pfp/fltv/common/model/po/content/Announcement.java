package pfp.fltv.common.model.po.content;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Setting;
import pfp.fltv.common.model.base.content.BaseEntity;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/14 下午 3:26:17
 * @description 公告的实体类(PO)
 * @filename Announcement.java
 */

@Setting(sortOrders = Setting.SortOrder.desc)
@Document(indexName = "announcement")
@TableName(value = "announcement", autoResultMap = true)
@Schema(description = "公告(PO实体类)")
public class Announcement extends BaseEntity {


}
