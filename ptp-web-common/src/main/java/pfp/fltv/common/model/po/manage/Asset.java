package pfp.fltv.common.model.po.manage;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfp.fltv.common.enums.AssetStatus;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/17 下午 9:59:07
 * @description 用户资产
 * @filename Asset.java
 */

@Schema(description = "用户资产")
@TableName("asset")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset implements Serializable {


    @Schema(description = "ID")
    private Long id;

    @Schema(description = "当前用户的余额")
    private Double balance;

    @Schema(description = "绑定的银行卡")
    private List<String> accounts;

    @Schema(description = "当前账户所允许的操作")
    private List<String> authorities;

    @Schema(description = "当前账户状态")
    private AssetStatus status;

    @Schema(description = "资产创建时间")
    private Timestamp createTime;

    @Schema(description = "(最后)修改时间")
    private Timestamp updateTime;

    @Schema(description = "资产信息是否已被删除")
    private Integer isDeleted;

    // TODO 用户交易记录


}
