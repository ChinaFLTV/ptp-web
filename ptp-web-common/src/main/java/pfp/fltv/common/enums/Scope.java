package pfp.fltv.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pfp.fltv.common.enums.base.ConvertableEnum;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/21 下午 8:17:54
 * @description 动作执行时所在的作用域
 * @filename Scope.java
 */

@Getter
@AllArgsConstructor
public enum Scope implements ConvertableEnum<Integer> {


    DIALOGUE(501, "对话"),
    PASSAGE(502, "文章"),
    PASSAGE_COMMENT(503, "文章评论"),
    PRIVATE_CHAT(504, "私聊"),
    GROUP_CHAT(505, "群聊");


    @JsonValue
    private final Integer code;
    private final String comment;


}
