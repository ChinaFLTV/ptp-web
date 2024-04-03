package pfp.fltv.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/18 上午 10:56:14
 * @description 性别枚举常量
 * @filename Gender.java
 */

@AllArgsConstructor
public enum Gender {

    MALE("男", "MALE", 0),
    FEMALE("女", "FEMALE", 1),
    SECRET("保密", "SECRET", 2);


    private final String message;
    private final String comment;
    private final Integer code;


    @JsonValue
    public Integer getCode() {

        return code;

    }


}
