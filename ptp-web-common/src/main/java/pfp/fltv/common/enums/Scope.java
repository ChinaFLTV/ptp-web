package pfp.fltv.common.enums;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/21 下午 8:17:54
 * @description 动作执行时所在的作用域
 * @filename Scope.java
 */

public enum Scope {


    DIALOGUE("对话", 501),
    PASSAGE("文章", 502),
    PASSAGE_COMMENT("文章评论", 503),
    PRIVATE_CHAT("私聊", 504),
    GROUP_CHAT("群聊", 505);


    private final String comment;
    private final Integer code;


    Scope(String comment, Integer code) {

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
