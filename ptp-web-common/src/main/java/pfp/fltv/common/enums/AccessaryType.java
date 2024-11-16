package pfp.fltv.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pfp.fltv.common.enums.base.ConvertableEnum;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/11/16 PM 6:12:23
 * @description 内容实体的附件类型的枚举类
 * @filename AccessaryType.java
 */

@Getter
@AllArgsConstructor
public enum AccessaryType implements ConvertableEnum<Integer> {


    NONE(2701, "无附件"),
    PHOTO(2702, "图片附件"),
    AUDIO(2703, "音频附件"),
    VIDEO(2704, "视频附件"),
    FILE(2705, "文件附件");


    @JsonValue
    private final Integer code;
    private final String comment;


}