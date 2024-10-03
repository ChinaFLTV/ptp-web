package pfp.fltv.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pfp.fltv.common.enums.base.ConvertableEnum;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/10/3 PM 1:27:23
 * @description 聊天类型枚举
 * @filename ChatType.java
 */

@Getter
@AllArgsConstructor
public enum ChatType implements ConvertableEnum<Integer> {


    GROUP_CHAT(2001, "群聊"),
    PRIVATE_CHAT(2002, "私聊");


    @JsonValue
    private final Integer code;
    private final String comment;


}
