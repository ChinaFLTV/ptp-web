package pfp.fltv.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pfp.fltv.common.enums.base.ConvertableEnum;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/21 下午 8:09:47
 * @description 标识用户的行为
 * @filename Action.java
 */

@Getter
@AllArgsConstructor
public enum Action implements ConvertableEnum<Integer> {


    LIKE(401, "点赞"),
    UNLIKE(402, "取消点赞"),
    STAR(403, "收藏"),
    UNSTAR(404, "取消收藏"),
    BROWSE(405, "浏览"),
    COMMENT(406, "评论"),
    SUBSCRIBE(407, "关注"),
    UNSUBSCRIBE(408, "取消关注");


    @JsonValue
    private final Integer code;
    private final String comment;


}
