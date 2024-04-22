package ptp.fltv.web.service.store.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import ptp.fltv.web.service.store.utils.FileUtils;

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


    /**
     * @param fileName 文件名称(也可以是包含文件名称的文件路径)
     * @return 较为准确的Content-Type
     * @author Lenovo/LiGuanda
     * @date 2024/4/22 PM 8:35:39
     * @version 1.0.0
     * @description 根据文件的拓展名获取确切的Content-Type
     * @filename ContentType.java
     */
    public String getExactContentType(@Nonnull String fileName) {

        if (StringUtils.isEmpty(fileName)) {

            return defaultContentType;

        }

        String fileExtension = FileUtils.fetchFileExtensionFromPath(fileName, true);

        if (candidateTypes.contains(fileExtension)) {

            return "application/" + fileExtension;

        } else {

            return defaultContentType;

        }

    }


}