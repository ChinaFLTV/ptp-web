package pfp.fltv.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/17 下午 10:12:40
 * @description 流水/任务状态枚举信息
 * @filename TaskStatus.java
 */

@Getter
@AllArgsConstructor
public enum TaskStatus {


    NORMAL(200, "正常"),

    SUSPEND(201, "挂起"),
    POSTPONE(202, "推迟"),
    TERMINATION(203, "终止"),
    HIDDEN(205, "隐藏"),
    ROLLBACK(206, "回滚"),
    UNDO(207, "撤销");


    @JsonValue
    private final Integer code;
    private final String comment;


}
