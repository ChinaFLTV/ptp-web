package ptp.fltv.web.service.store.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/21 PM 10:02:42
 * @description 对象所属的文件类型
 * @filename ContentType.java
 */

@AllArgsConstructor
@Getter
public enum ContentType {


    PLAIN_TEXT(1001, "text/plain", Set.of("txt", "md", "html", "css", "javascript")),
    AUDIO(1002, "audio/wav", Set.of("mpeg", "ogg", "wav", "midi")),
    IMAGE(1003, "image/jpeg", Set.of("gif", "jpg", "jpeg", "png", "webp", "svg+xml", "x-icon")),
    VIDEO(1004, "video/mp4", Set.of("mp4", "ogg", "webm", "x-flv")),
    APPLICATION(1005, "application/octet-stream", Set.of("xml", "json", "pdf", "zip", "msword", "vnd.ms-excel", "vnt.ms-powerpoint", "octet-stream", "x-www-form-urlencoded")),
    UNKNOWN(1006, "application/octet-stream", Set.of());


    @JsonValue
    private final Integer code;
    private final String defaultContentType;
    private final Set<String> candidateTypes;


}