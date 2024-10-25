package pfp.fltv.common.model.po.manage;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.apache.ibatis.type.JdbcType;
import pfp.fltv.common.model.base.system.BaseEntity;
import pfp.fltv.common.model.po.content.Comment;
import pfp.fltv.common.model.po.content.Passage;

import java.util.List;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/10/25 PM 7:56:57
 * @apiNote 用户的评分记录以及某个内容实体总共被评分的统计将被统一放置于此
 * @description 内容评分实体(PO)
 * @filename Rate.java
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Accessors(chain = true)
@TableName(value = "rate", autoResultMap = true)
public class Rate extends BaseEntity {


    @Schema(description = "内容实体类型")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Comment.BelongType contentType; // 2024-10-25  20:03-内容实体类型

    @Schema(description = "内容实体ID")
    private Long contentId; // 2024-10-25  20:03-内容实体ID

    @Schema(description = "内容实体标题(如果有的话)")
    private String contentTitle; // 2024-10-25  20:08-内容实体标题(如果有的话)

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(description = "内容实体的评分标签")
    private List<String> contentTags; // 2024-10-25  20:10-内容实体的评分标签(评分统计类型与用户评分类型的评分记录公用)(这里的标签不是内容发布者主动打的标签)

    @Schema(description = "平均评分")
    private Double averageScore; // 2024-10-25  20:11-平均评分(评分统计类型的评分记录所需)

    @Schema(description = "最高评分")
    private Double maxScore; // 2024-10-25  20:12-最高评分(评分统计类型的评分记录所需)

    @Schema(description = "最低评分")
    private Double minScore; // 2024-10-25  20:13-最低评分(评分统计类型的评分记录所需)

    @Schema(description = "评分人数")
    private Long rateUserCount; // 2024-10-25  20:04-评分人数(评分统计类型的评分记录所需)

    // 2024-10-25  20:34-该Map中的Key为N.X(意为评分为1) , 值为该内容的总评分在该区间的人数
    @TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.LONGVARCHAR)
    @Schema(description = "评分人数分布映射")
    private Map<String, Integer> rateUserCountMap; // 2024-10-25  20:15-评分人数分布映射(类似于 : 1.X to 9999 , 2.X to 1234 , 3.X to 5678)(评分统计类型的评分记录所需)

    // 2024-10-25  20:33-该Map中的Key为pfp.fltv.common.model.po.content.Passage.Standard枚举类的成员的code , 值为具体的评分(评分最终要换算成满分为10.0的浮点数值)
    @TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.LONGVARCHAR)
    @Schema(description = "具体的评分详情")
    private Map<String, Double> rateMap; // 2024-10-25  20:22-具体的评分详情(类似于 : Passage.Standard.AUTHENTICITY to 6.8 , Passage.Standard.ACCURACY to 3.4 , Passage.Standard.OBJECTIVITY to 9.2)


}
