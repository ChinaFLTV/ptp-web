package pfp.fltv.common.enums;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/24 下午 7:45:30
 * @description 用户资产状态枚举信息
 * @filename AssetStatus.java
 */

public enum AssetStatus {


    NORMAL("正常", 700),
    ABNORMAL("异常", 701),
    LIMIT("限制", 702),
    WARN("警告", 703),
    BLOCK("封禁", 704);


    private final String comment;
    private final Integer code;


    AssetStatus(String comment, Integer code) {

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
