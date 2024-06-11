package pfp.fltv.common.utils;

import jakarta.annotation.Nonnull;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/11 PM 11:32:22
 * @description 关于字符串的工具类
 * @filename StringUtils.java
 */

public class StringUtils {


    /**
     * @param originStr   待填充的原始字符串
     * @param placeholder 填充字符
     * @param length      填充后的长度
     * @return 填充后的字符串
     * @author Lenovo/LiGuanda
     * @date 2024/6/11 PM 11:34:40
     * @version 1.0.0
     * @description 将指定字符串按指定填充字符填充至指定位数(如果指定的填充长度小于等于当前原始字符串的长度 ， 则不做任何操作)
     * @filename StringUtils.java
     */
    public static String padToBytes(@Nonnull String originStr, @Nonnull Character placeholder, @Nonnull Integer length) {

        if (originStr.length() >= length) {

            return originStr;

        }

        StringBuilder stringBuilder = new StringBuilder(originStr);
        stringBuilder.append(String.valueOf(placeholder)
                .repeat(length - originStr.length()));

        return stringBuilder.toString();

    }


}
