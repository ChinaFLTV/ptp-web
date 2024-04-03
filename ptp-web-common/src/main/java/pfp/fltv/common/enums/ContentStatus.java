package pfp.fltv.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/17 下午 9:10:09
 * @description 内容状态状态枚举信息
 * @filename Status.java
 */

public enum ContentStatus {


    NORMAL("正常", 100),
    ABNORMAL("异常", 101),
    ERROR("错误", 102),
    REJECT("打回", 103),
    LIMIT("限流", 104),
    BLOCK("锁定", 105),
    HIDDEN("隐藏", 106),
    DELETED("删除", 107),
    FORBIDDEN("封禁", 107);


    private final String comment;
    private final Integer code;


    ContentStatus(String comment, Integer code) {

        this.comment = comment;
        this.code = code;

    }


    public String getComment() {

        return comment;

    }


    @JsonValue
    public Integer getCode() {

        return code;

    }


}
