package ptp.fltv.web.constants;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/28 PM 8:54:20
 * @description 用于存放Web相关的常量
 * @filename WebConstants.java
 */

public class WebConstants {


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/4/28 PM 8:56:16
     * @version 1.0.0
     * @description ES服务的基础路径
     * @filename WebConstants.java
     */
    public static final String ES_BASE_URL = "http://127.0.0.1:8120";


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/4/28 PM 8:55:04
     * @version 1.0.0
     * @description ES服务的上下文基础路径
     * @filename WebConstants.java
     */
    public static final String ES_CONTEXT_URL = "/api/v1/es";


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/4/28 PM 9:13:41
     * @version 1.0.0
     * @description ES服务的各种控制器的一级路径
     * @filename WebConstants.java
     */
    public static final String ES_BASE_ANNOUNCEMENT_URL = "/content/announcement";
    public static final String ES_BASE_DIALOGUE_URL = "/content/dialogue";
    public static final String ES_BASE_PASSAGE_URL = "/content/passage";
    public static final String ES_BASE_PASSAGE_COMMENT_URL = "/content/passage/comment";
    public static final String ES_BASE_USER_URL = "/content/user";


}
