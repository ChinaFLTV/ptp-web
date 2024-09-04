package pfp.fltv.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pfp.fltv.common.enums.base.ConvertableEnum;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/19 PM 11:25:11
 * @description 商品状态的枚举类
 * @filename CommodityStatus.java
 */

@Getter
@AllArgsConstructor
public enum CommodityStatus implements ConvertableEnum<Integer> {


    ON_SALE(1201, "售卖中"),
    SOLD_OUT(1202, "售罄"),
    BLOCKED(1203, "冻结中"),
    ADEQUATE_STOCK(1204, "库存充足"),
    UNDERSTOCK(1205, "库存不足"),
    HOT_SALE(1206, "热卖中"),
    HIGHLY_PRAISE(1207, "超高评价"),
    NEW_ARRIVAL(1208, "新品"),
    REPLENISHING(1209, "补货中");


    @EnumValue
    @JsonValue
    private final Integer code;
    private final String comment;


}
