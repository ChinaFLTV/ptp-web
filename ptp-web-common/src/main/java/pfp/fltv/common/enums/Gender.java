package pfp.fltv.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pfp.fltv.common.enums.base.ConvertableEnum;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/18 上午 10:56:14
 * @description 性别枚举常量
 * @filename Gender.java
 */

@Getter
@AllArgsConstructor
public enum Gender implements ConvertableEnum<Integer> {


    MALE(0, "男", "MALE"),
    FEMALE(1, "女", "FEMALE"),
    SECRET(2, "保密", "SECRET");


    @JsonValue
    private final Integer code;
    private final String message;
    private final String comment;


}
