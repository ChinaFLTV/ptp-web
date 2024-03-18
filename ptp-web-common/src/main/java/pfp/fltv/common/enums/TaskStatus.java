package pfp.fltv.common.enums;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/17 下午 10:12:40
 * @description 流水/任务状态枚举信息
 * @filename TaskStatus.java
 */

public enum TaskStatus {


    NORMAL("正常", 200),

    SUSPEND("挂起", 201),
    POSTPONE("推迟", 202),
    TERMINATION("终止", 203),
    HIDDEN("隐藏", 205),
    ROLLBACK("回滚", 206),
    UNDO("撤销", 207);


    private final String comment;
    private final Integer code;


    TaskStatus(String comment, Integer code) {

        this.comment = comment;
        this.code = code;

    }


    public String getComment() {

        return comment;

    }


    public Integer getCode() {

        return code;

    }


}
