package pfp.fltv.common.enums;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/22 下午 6:24:49
 * @description 响应信息的状态枚举信息
 * @filename ResponseStatus.java
 */

public enum ResponseStatus {


    SUCCESS("响应成功", 601),
    FAIL("响应失败", 602),
    ABNORMAL("响应被终止", 603),
    INTERRUPTED("响应被拦截", 604),
    BLOCKED("响应被阻止", 605);


    private final String comment;
    private final Integer code;


    ResponseStatus(String comment, Integer code) {

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
