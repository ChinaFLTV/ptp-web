package pfp.fltv.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pfp.fltv.common.enums.base.ConvertableEnum;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/24 下午 7:45:30
 * @description 用户资产状态枚举信息
 * @filename AssetStatus.java
 */

@Getter
@AllArgsConstructor
public enum AssetStatus implements ConvertableEnum<Integer> {


    NORMAL(700, "正常"),
    ABNORMAL(701, "异常"),
    LIMIT(702, "限制"),
    WARN(703, "警告"),
    BLOCK(704, "封禁");


    @JsonValue
    private final Integer code;
    private final String comment;


}
