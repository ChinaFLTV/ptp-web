package pfp.fltv.common.enums;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/18 上午 10:56:14
 * @description 性别枚举常量
 * @filename Gender.java
 */

public enum Gender {

    MALE("男", 0),
    FEMALE("女", 1),
    SECRET("保密", 2);


    private final String comment;
    private final Integer code;


    Gender(String comment, Integer code) {

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
