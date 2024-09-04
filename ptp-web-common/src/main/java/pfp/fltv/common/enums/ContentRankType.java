package pfp.fltv.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pfp.fltv.common.enums.base.ConvertableEnum;

import java.util.Objects;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/21 PM 12:36:39
 * @description 内容实体的排名类型的枚举常量
 * @filename ContentRankType.java
 */

@AllArgsConstructor
@Getter
public enum ContentRankType implements ConvertableEnum<Integer> {


    TOTAL_100(1601, "rank100", "总排行TOP100"),
    UNKNOWN(1602, "unknown", "未知排行");


    @JsonValue
    private final Integer code;
    private final String subkey; // 2024-6-21  12:38-用于参与拼接成Redis的内容实体排行榜的key
    private final String comment;


    /**
     * @param code 需要被转换的code值
     * @return 转换后的对应的LoginClientType类型的枚举常量
     * @author Lenovo/LiGuanda
     * @date 2024/6/21 PM 12:40:54
     * @version 1.0.0
     * @description 将指定整形类型的code值转换为对应的LoginClientType枚举常量
     * @filename ContentRankType.java
     */
    public static ContentRankType valueOfByCode(@Nonnull Integer code) {

        for (ContentRankType contentRankType : values()) {

            if (Objects.equals(contentRankType.code, code)) {

                return contentRankType;

            }

        }

        return UNKNOWN;

    }


}