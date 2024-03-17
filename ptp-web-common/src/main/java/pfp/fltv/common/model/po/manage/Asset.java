package pfp.fltv.common.model.po.manage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/17 下午 9:59:07
 * @description 用户资产
 * @filename Asset.java
 */

@Schema(description = "用户资产")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset {


    @Schema(description = "当前用户的余额")
    private Double balance;

    // TODO 用户交易记录


}
