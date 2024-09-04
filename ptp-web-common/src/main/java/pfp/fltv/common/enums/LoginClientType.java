package pfp.fltv.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pfp.fltv.common.enums.base.ConvertableEnum;

import java.util.Objects;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/17 PM 10:10:45
 * @description 用户登录所使用的客户端的类型枚举
 * @filename LoginClientType.java
 */

@Getter
@AllArgsConstructor
public enum LoginClientType implements ConvertableEnum<Integer> {


    WEB(1401, "网页端"),
    MOBILE(1402, "移动端"),
    PC(1403, "桌面端"),
    UNKNOWN(1401, "未知");


    @JsonValue
    private final Integer code;
    private final String comment;


    /**
     * @param code 需要被转换的code值
     * @return 转换后的对应的LoginClientType类型的枚举常量
     * @author Lenovo/LiGuanda
     * @date 2024/6/18 PM 8:15:16
     * @version 1.0.0
     * @description 将指定整形类型的code值转换为对应的LoginClientType枚举常量
     * @filename LoginClientType.java
     */
    public static LoginClientType valueOfByCode(@Nonnull Integer code) {

        for (LoginClientType loginClientType : values()) {

            if (Objects.equals(loginClientType.code, code)) {

                return loginClientType;

            }

        }

        return UNKNOWN;

    }


}
