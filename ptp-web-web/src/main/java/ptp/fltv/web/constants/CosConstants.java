package ptp.fltv.web.constants;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/8/11 PM 5:48:15
 * @description 用于存放腾讯云对象存储COS相关的常量
 * @filename CosConstants.java
 */

public class CosConstants {


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/8/11 PM 5:49:36
     * @version 1.0.0
     * @description 专门用于存放用户图片的存储桶名称
     * @filename CosConstants.java
     */
    public static final String BUCKET_USER_PICTURE = "ptp-user-picture-1309498949";
    /**
     * @author Lenovo/LiGuanda
     * @date 2024/8/11 PM 7:23:48
     * @version 1.0.0
     * @description 云端资源文件的访问URL格式前缀串(该模式串中的两个 % s需分别用存储桶名称和存储对象key替换)
     * @filename CosConstants.java
     */
    public static final String PUBLIC_REQUEST_URL_PREFIX = "https://%s.cos.ap-nanjing.myqcloud.com%s";

    /**
     * @author Lenovo/LiGuanda
     * @date 2024/8/31 PM 9:33:10
     * @version 1.0.0
     * @description 聊天室中产生的多媒体数据的存放的存储桶名称
     * @filename CosConstants.java
     */
    public static final String BUCKET_CHAT_ROOM = "ptp-chat-room-1309498949";
    /**
     * @author Lenovo/LiGuanda
     * @date 2024/8/30 PM 11:55:50
     * @version 1.0.0
     * @description 聊天消息相关的多媒体数据存放的存储桶下的KEY(桶内的相对路径)
     * @filename CosConstants.java
     */
    public static final String BUCKET_CHAT_ROOM_PHOTO = "/photo";
    public static final String BUCKET_CHAT_ROOM_VIDEO = "/video";
    public static final String BUCKET_CHAT_ROOM_AUDIO = "/audio";
    public static final String BUCKET_CHAT_ROOM_VOICE = "/voice";
    public static final String BUCKET_CHAT_ROOM_FILE = "/file";


}