package pfp.fltv.common.model.po.manage;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pfp.fltv.common.model.base.system.BaseEntity;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/10/21 PM 5:03:01
 * @description 订阅关系数据实体(PO)
 * @filename SubscriberShip.java
 */

@TableName(value = "subscriber_ship", autoResultMap = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SubscriberShip extends BaseEntity {


    @Schema(description = "订阅者的ID(也即订阅操作发起者)")
    private Long followerId;

    @Schema(description = "被订阅者的ID")
    private Long followeeId;


}