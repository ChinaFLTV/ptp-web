package pfp.fltv.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/18 上午 10:56:14
 * @description 性别枚举常量
 * @filename Gender.java
 */

@AllArgsConstructor
@Getter
public enum Gender {

    MALE("男", "MALE", 0),
    FEMALE("女", "FEMALE", 1),
    SECRET("保密", "SECRET", 2);


    private final String message;
    @JsonValue
    private final String comment;
    private final Integer code;


    @JsonCreator
    public static Gender get(String comment) {

        for (Gender gender : values()) {

            if (Objects.equals(gender.getComment(), comment)) {

                return gender;

            }

        }

        return null;

    }


}
