package pfp.fltv.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pfp.fltv.common.enums.base.ConvertableEnum;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/11/26 PM 6:08:03
 * @description 登录方式的枚举类
 * @filename LoginType.java
 */

@Getter
@AllArgsConstructor
public enum LoginType implements ConvertableEnum<Integer> {


    PTP(2801, "PTP账号登录(用户昵称+密码)"),
    QQ(2802, "QQ登录(用户唯一标识ID)"),
    WECHAT(2803, "微信登录(用户唯一标识ID)"),
    WEIBO(2804, "微博登录(用户唯一标识ID)");


    @JsonValue
    private final Integer code;
    private final String comment;


}