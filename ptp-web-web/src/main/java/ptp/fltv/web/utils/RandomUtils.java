package ptp.fltv.web.utils;

import com.alibaba.fastjson2.JSONObject;
import io.github.yindz.random.RandomSource;

import java.util.Random;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/11/24 AM 11:05:27
 * @description 用于生成一些随机内容的工具类
 * @filename RandomUtils.java
 */

public class RandomUtils {


    private static final Random random = new Random(); // 2024-12-1  23:35-请不要使用固定的种子数 , 否则极有可能在账号生成初期发生大量碰撞(你也可以设置 , 但请务必保证每次的种子数不一致(在随机器初始化的时候))


    /**
     * @return 一个随机的10位的纯数字的账号字符串
     * @author Lenovo/LiGuanda
     * @date 2024/11/24 AM 11:05:41
     * @version 1.0.0
     * @description 用于生成一个10位的纯数字的账号字符串
     * @filename RandomUtils.java
     */
    public static String generateAccount() {


        StringBuilder builder = new StringBuilder();

        for (int i = 1; i <= 10; i++) {

            int num = random.nextInt(10);
            builder.append(num);

        }

        return builder.toString();

    }


    /**
     * @return 一个随机的头像图片JSON字符串内容
     * @author Lenovo/LiGuanda
     * @date 2024/11/24 AM 11:08:03
     * version 1.0.0
     * @description 用于生成一个头像图片JSON字符串内容(主要用于用户头像的初始化设置)
     * @filename RandomUtils.java
     */
    public static String generateAvatar() {

        JSONObject avatarJsonObj = new JSONObject();
        avatarJsonObj.put("type", "url");

        String backgroundColor = generateColor();
        String foregroundColor = generateColor();
        avatarJsonObj.put("uri", "https://dummyimage.com/512x512/" + backgroundColor.substring(1) + "/" + foregroundColor.substring(1) + ".png&text=" + RandomSource.personInfoSource().randomChineseNickName(8));

        return avatarJsonObj.toJSONString();

    }


    /**
     * @return 一个随机的背景图片JSON字符串内容
     * @author Lenovo/LiGuanda
     * @date 2024/11/24 AM 11:26:08
     * @version 1.0.0
     * @description 用于生成一个背景图片JSON字符串内容(主要用于用户背景的初始化设置)
     * @filename RandomUtils.java
     */
    public static String generateBackground() {

        JSONObject backgroundJsonObj = new JSONObject();
        backgroundJsonObj.put("type", "url");

        String backgroundColor = generateColor();
        String foregroundColor = generateColor();
        backgroundJsonObj.put("uri", "https://dummyimage.com/1024x512/" + backgroundColor.substring(1) + "/" + foregroundColor.substring(1) + ".png&text=" + RandomSource.personInfoSource().randomChineseNickName(8));

        return backgroundJsonObj.toJSONString();

    }


    /**
     * @return 一个随机的六位16进制的颜色数值字符串(例如 : # ff00ff)
     * @author Lenovo/LiGuanda
     * @date 2024/11/24 AM 11:15:16
     * @version 1.0.0
     * @description 专门用来生成一个随机的六位16进制的颜色数值字符串
     * @filename RandomUtils.java
     */
    public static String generateColor() {

        StringBuilder builder = new StringBuilder();
        builder.append("#");

        for (int i = 0; i < 3; i++) {

            int val = random.nextInt(256);
            String valStr = Integer.toHexString(val).toLowerCase();
            if (valStr.length() == 1) {

                valStr = "0" + valStr;

            }
            builder.append(valStr);

        }

        return builder.toString();

    }


}