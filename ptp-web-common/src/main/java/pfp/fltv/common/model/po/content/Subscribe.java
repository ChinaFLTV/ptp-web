package pfp.fltv.common.model.po.content;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/18 上午 11:12:47
 * @description 用户之间的订阅实体类(POJO)
 * @filename Subscribe.java
 */

@Schema(description = "用户订阅信息")
@TableName("subscribe")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscribe {


    @Schema(description = "订阅信息ID")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "订阅发起者ID")
    private Long followerId;

    @Schema(description = "被订阅者ID")
    private Long subscriberId;

    @Schema(description = "订阅时间")
    private Timestamp createTime;

    @Schema(description = "(最后)更新时间")
    private Timestamp updateTime;

    @Schema(description = "订阅坐标", example = "passage.java.design_mode")
    private String coordinate;

    @Schema(description = "其他数据配置(JSON)")
    private String meta;


}
