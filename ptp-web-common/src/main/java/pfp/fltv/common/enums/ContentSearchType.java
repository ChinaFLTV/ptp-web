package pfp.fltv.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pfp.fltv.common.enums.base.ConvertableEnum;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/14 下午 8:30:20
 * @description 内容查询类型的枚举类
 * @filename ContentSearchType.java
 */

@Getter
@AllArgsConstructor
public enum ContentSearchType implements ConvertableEnum<Integer> {


    DIALOGUE(901, "对话"),
    PASSAGE(902, "文章"),
    PASSAGE_COMMENT(903, "文章评论"),
    USER(904, "用户"),
    ROLE(904, "角色"),
    ASSET(905, "财产");


    @JsonValue
    private final Integer code;
    private final String comment;


}
