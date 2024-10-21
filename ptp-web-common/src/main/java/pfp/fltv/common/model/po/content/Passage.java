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


    @Schema(description = "文章发布者昵称")
    private String publisherNickname; // 2024-10-21  21:33-增加 文章发布者昵称 字段

    @Schema(description = "文章发布者头像URL")
    private String publisherAvatarUrl; // 2024-10-21  21:33-增加 文章发布者头像URL 字段

    @Schema(description = "内容介绍", maxLength = 35)
    private String introduction;

    @Schema(description = "文章封面图片资源URL")
    private String coverImgUrl; // 2024-10-21  16:36-增加 文章封面图片资源URL 字段


}
