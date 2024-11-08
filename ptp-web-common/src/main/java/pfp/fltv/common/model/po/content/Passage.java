package pfp.fltv.common.model.po.content;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
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

    @Schema(description = "评分记录统计ID(建议在创建文章记录时 , 同步新增对应的评分统计记录)")
    private long rateId; // 2024-10-25  20:06-增加 评分记录统计ID 字段(建议在创建文章记录时 , 同步新增对应的评分统计记录)


    /**
     * @author Lenovo/LiGuanda
     * @version 1.0.0
     * @date 2024/10/25 PM 8:22:59
     * @description 文章评分标准的常量枚举类
     * @filename Passage.java
     */
    @Getter
    @AllArgsConstructor
    public enum Standard {


        AUTHENTICITY(2201, "可靠性"),
        ACCURACY(2202, "准确性"),
        OBJECTIVITY(2203, "客观性"),
        DEPTH(2204, "深刻性"),
        LOGICALITY(2205, "逻辑性"),
        TIMELINESS(2206, "时效性");


        @JsonValue
        private final Integer code;
        private final String comment;


    }


}
