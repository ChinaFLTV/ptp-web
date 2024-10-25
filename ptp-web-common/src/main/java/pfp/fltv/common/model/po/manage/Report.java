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
import pfp.fltv.common.model.base.content.BaseEntity;
import pfp.fltv.common.model.po.content.Comment;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/10/17 PM 12:45:30
 * @description 内容举报实体(PO)
 * @apiNote 该实体主要存放在后端云中 , 很少存放在本地数据库中
 * @filename Report.java
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
// @Builder
@SuperBuilder // 2024-10-17  20:02-解决 pfp.fltv.common.model.po.manage.Report 中的 builder() 无法隐藏 pfp.fltv.common.model.base.content.BaseEntity 中的 builder()... 的问题
@Accessors(chain = true)
@TableName(value = "report", autoResultMap = true)
public class Report extends BaseEntity {


    @Schema(description = "内容实体类型")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Comment.BelongType contentType; // 2024-10-17  17:26-内容实体类型

    @Schema(description = "内容实体ID")
    private Long contentId; // 2024-10-17  17:27-内容实体ID

    @Schema(description = "举报者昵称")
    private String nickname; // 2024-10-17  17:38-举报的用户昵称

    @Schema(description = "举报者头像URL")
    private String avatarUrl; // 2024-10-17  17:38-举报的用户头像URL


}