package pfp.fltv.common.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfp.fltv.common.enums.AssetStatus;

import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 8:08:47
 * @description 前端传输过来的Asset的部分数据
 * @filename AssetVo.java
 */

@Schema(description = "前端传输过来的Asset的部分数据")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetVo {


    @Schema(description = "ID")
    private Long id;

    @Schema(description = "当前用户的余额")
    private Double balance;

    @Schema(description = "绑定的银行卡")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> accounts;

    @Schema(description = "当前账户所允许的操作")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> authorities;

    @Schema(description = "当前账户的信誉积分")
    private Double credit;

    @Schema(description = "当前账户状态")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private AssetStatus status;


}
