package pfp.fltv.common.model.po.manage;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import pfp.fltv.common.enums.AssetStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/17 下午 9:59:07
 * @description 用户资产
 * @filename Asset.java
 */

@Schema(description = "用户资产")
@TableName(value = "asset", autoResultMap = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset implements Serializable {


    @Schema(description = "ID")
    private Long id;

    @Schema(description = "当前用户的余额")
    private Double balance = 0D;

    @Schema(description = "绑定的银行卡")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> accounts = new ArrayList<>();

    @Schema(description = "当前账户所允许的操作")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> authorities = new ArrayList<>();

    @Schema(description = "当前账户的信誉积分")
    private Double credit = 100D;

    @Schema(description = "当前账户状态")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private AssetStatus status = AssetStatus.NORMAL;

    @Schema(description = "资产创建时间")
    private LocalDateTime createTime;

    @Schema(description = "(最后)修改时间")
    private LocalDateTime updateTime;

    @Schema(description = "资产信息是否已被删除")
    private Integer isDeleted = 0;

    @Transient
    @Schema(description = "当前实体的版本(用于辅助实现乐观锁)")
    @Version
    private Integer version = 1;


}
