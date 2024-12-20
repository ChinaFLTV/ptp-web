package pfp.fltv.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pfp.fltv.common.enums.base.ConvertableEnum;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/17 下午 9:10:09
 * @description 内容状态状态枚举信息
 * @filename Status.java
 */

@Getter
@AllArgsConstructor
public enum ContentStatus implements ConvertableEnum<Integer> {


    NORMAL(100, "正常"),
    ABNORMAL(101, "异常"),
    ERROR(102, "错误"),
    REJECT(103, "打回"),
    LIMIT(104, "限流"),
    BLOCK(105, "锁定"),
    HIDDEN(106, "隐藏"),
    DELETED(107, "删除"),
    FORBIDDEN(107, "封禁"),
    VISIBLE_ONLY_TO_FANS(108, "仅粉丝可见"),
    VISIBLE_ONLY_TO_FRIENDS(109, "仅朋友可见");


    @JsonValue
    private final Integer code;
    private final String comment;


}
