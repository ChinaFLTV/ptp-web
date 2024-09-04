package pfp.fltv.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pfp.fltv.common.enums.base.ConvertableEnum;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/23 PM 8:49:58
 * @description 媒体内容操作类型的枚举类
 * @filename MediaOperationType.java
 */

@Getter
@AllArgsConstructor
public enum MediaOperationType implements ConvertableEnum<Integer> {


    GET_STREAM(1101, "get the input stream of a file"),
    DOWNLOAD(1102, "download a file"),
    DOWNLOAD_BATCH(1103, "download multiple files in a batch"),
    UPDATE(1104, "update a file"),
    DELETE(1105, "delete a file"),
    DELETE_BATCH(1106, "delete multiple files in a batch");


    @JsonValue
    private final Integer code;
    private final String comment;


}
