package pfp.fltv.common.enums;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/21 下午 8:09:47
 * @description 标识用户的行为
 * @filename Action.java
 */

public enum Action {


    LIKE("点赞", 401),
    UNLIKE("取消点赞", 402),
    STAR("收藏", 403),
    UNSTAR("取消收藏", 404),
    BROWSE("浏览", 405),
    COMMENT("评论", 406),
    SUBSCRIBE("关注", 407),
    UNSUBSCRIBE("取消关注", 408);


    private final String comment;
    private final Integer code;


    Action(String comment, Integer code) {

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
