package pfp.fltv.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pfp.fltv.common.enums.base.ConvertableEnum;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/17 下午 10:11:32
 * @description 用户状态枚举信息
 * @filename UserStatus.java
 */

@Getter
@AllArgsConstructor
public enum UserStatus implements ConvertableEnum<Integer> {


    NORMAL(300, "正常"),
    ABNORMAL(301, "异常"),
    LIMIT(302, "限制"),
    HIDDEN(303, "不可见"),
    BLOCK(304, "锁定");


    @JsonValue
    private final Integer code;
    private final String comment;


}
