package pfp.fltv.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pfp.fltv.common.enums.base.ConvertableEnum;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/10/2 AM 12:37:53
 * @description 数据实体查询时的排序类型枚举
 * @filename ContentQuerySortType.java
 */

@AllArgsConstructor
@Getter
public enum ContentQuerySortType implements ConvertableEnum<Integer> {


    LATEST(1901, "发布时间"),
    HOTTEST(1902, "综合热度"),
    LIKE(1903, "点赞量"),
    STAR(1904, "收藏量"),
    BROWSE(1905, "浏览量"),
    COMMENT(1906, "评论量"),
    SHARE(1907, "转发量"),
    OWNER(1908,"内容所有者"),
    DEFAULT(1909, "默认");


    @JsonValue
    private final Integer code;
    private final String comment;


}
