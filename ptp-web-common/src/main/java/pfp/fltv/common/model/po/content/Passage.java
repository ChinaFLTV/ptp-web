package pfp.fltv.common.model.po.content;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Setting;
import pfp.fltv.common.model.base.content.BaseEntity;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/14 下午 4:03:20
 * @description 文章的实体类(PO)
 * @filename Passage.java
 */

@Setting(sortOrders = Setting.SortOrder.desc)
@Document(indexName = "passage")
@TableName(value = "passage", autoResultMap = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Passage extends BaseEntity {


    @Schema(description = "内容介绍", maxLength = 35)
    private String introduction;


}
