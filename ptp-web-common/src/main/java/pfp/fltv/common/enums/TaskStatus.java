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


    NORMAL("正常", 200),

    SUSPEND("挂起", 201),
    POSTPONE("推迟", 202),
    TERMINATION("终止", 203),
    HIDDEN("隐藏", 205),
    ROLLBACK("回滚", 206),
    UNDO("撤销", 207);


    private final String comment;
    @JsonValue
    private final Integer code;


}
