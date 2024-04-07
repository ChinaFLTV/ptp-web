package pfp.fltv.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/17 下午 10:11:32
 * @description 用户状态枚举信息
 * @filename UserStatus.java
 */

@Getter
@AllArgsConstructor
public enum UserStatus {


    NORMAL("正常", 300),
    ABNORMAL("异常", 301),
    LIMIT("限制", 302),
    HIDDEN("不可见", 303),
    BLOCK("锁定", 304);


    private final String comment;
    @JsonValue
    private final Integer code;


}
