package pfp.fltv.common.utils;

import jakarta.annotation.Nonnull;
import org.springframework.util.StringUtils;
import pfp.fltv.common.enums.ContentType;

import java.util.Set;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/21 PM 10:23:15
 * @description 文件操作工具类
 * @filename FileUtils.java
 */

public class FileUtils {


    /**
     * @param path      文件路径
     * @param lowercase 是否将文件拓展名小写化(false则返回大写形式)
     * @return 提取出来的文件拓展名(路径非法返回null)
     * @author Lenovo/LiGuanda
     * @date 2024/4/21 PM 10:25:38
     * @version 1.0.0
     * @description 从文件路径中提取文件拓展名
     * @filename FileUtils.java
     */
    public static String fetchFileExtensionFromPath(String path, boolean lowercase) {

        if (!StringUtils.hasLength(path)) {

            return null;

        }

        int index = path.lastIndexOf('.');
        String fileExtension = path.substring(index + 1);

        return lowercase ? fileExtension.toLowerCase() : fileExtension.toUpperCase();

    }


    /**
     * @param fileExtension 文件拓展名
     * @return 根据文件拓展名转换后的Content-Type
     * @author Lenovo/LiGuanda
     * @date 2024/4/21 PM 10:31:53
     * @version 1.0.0
     * @description 根据文件拓展名转换为合适的ContentType
     * @filename FileUtils.java
     */
    public static ContentType fileExtension2ContentType(@Nonnull String fileExtension) {

        if (!StringUtils.hasLength(fileExtension)) {

            return ContentType.UNKNOWN;

        }

        for (ContentType contentType : ContentType.values()) {

            Set<String> candidateTypes = contentType.getCandidateTypes();
            if (candidateTypes.contains(fileExtension.toLowerCase())) {

                return contentType;

            }

        }

        return ContentType.UNKNOWN;

    }


    /**
     * @param storePath           文件的URI
     * @param retainFileExtension 是否保留文件的拓展名
     * @return 提取出来的文件名称(无法提取则返回一个与当前时间相关的字符串)
     * @author Lenovo/LiGuanda
     * @date 2024/4/23 PM 9:37:20
     * @version 1.0.0
     * @description 从指定文件URI中获取文件名
     * @filename FileUtils.java
     */
    public static String fetchFileName(@Nonnull String storePath, boolean retainFileExtension) {

        int lastSlashIndex = storePath.lastIndexOf('/');
        int lastPointIndex = storePath.lastIndexOf('.');

        if (lastSlashIndex == -1) {

            return String.format("download-file-%d", System.currentTimeMillis());

        }

        if (lastPointIndex == -1) {

            return storePath.substring(lastSlashIndex + 1);

        }

        return retainFileExtension ? storePath.substring(lastSlashIndex + 1) : storePath.substring(lastSlashIndex + 1, lastPointIndex);

    }


}
