package pfp.fltv.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/17 PM 10:10:45
 * @description 用户登录所使用的客户端的类型枚举
 * @filename LoginClientType.java
 */

@Getter
@AllArgsConstructor
public enum LoginClientType {


    WEB(1401, "网页端"),
    MOBILE(1402, "移动端"),
    PC(1403, "桌面端"),
    UNKNOWN(1401, "未知");


    @JsonValue
    private final Integer code;
    private final String comment;


}
