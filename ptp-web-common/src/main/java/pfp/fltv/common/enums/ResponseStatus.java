package pfp.fltv.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/22 下午 6:24:49
 * @description 响应信息的状态枚举信息
 * @filename ResponseStatus.java
 */

@Getter
@AllArgsConstructor
public enum ResponseStatus {


    SUCCESS(601, "响应成功"),
    FAIL(602, "响应失败"),
    ABNORMAL(603, "响应被终止"),
    INTERRUPTED(604, "响应被拦截"),
    BLOCKED(605, "响应被阻止");


    @JsonValue
    private final Integer code;
    private final String comment;


}
