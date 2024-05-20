package pfp.fltv.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/19 PM 11:25:11
 * @description 商品状态的枚举类
 * @filename CommodityStatus.java
 */

@Getter
@AllArgsConstructor
public enum CommodityStatus {


    ON_SALE(1201, "售卖中"),
    SOLD_OUT(1202, "售罄"),
    ;


    @JsonValue
    private final Integer code;
    private final String comment;


}
