package pfp.fltv.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pfp.fltv.common.enums.base.ConvertableEnum;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/11/5 AM 1:21:32
 * @description 内容的形式枚举类
 * @filename ContentFormat.java
 */

@Getter
@AllArgsConstructor
public enum ContentFormat implements ConvertableEnum<Integer> {


    BARE(2501, "立即值"),
    LOCAL_URL(2502, "本地资源直链"),
    CLOUD_URL(2503, "云端资源直链");


    @EnumValue
    @JsonValue
    private final Integer code;
    private final String comment;


}