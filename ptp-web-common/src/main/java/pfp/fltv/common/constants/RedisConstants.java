package pfp.fltv.common.constants;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/7 下午 10:14:16
 * @description 记录Redis相关的参数常量
 * @filename RedisConstants.java
 */

public class RedisConstants {


    // 2024-9-11  23:00-Redis中的用户身份信息的最大缓存时间应与客户端本地的Cookie存活时间一致 , 均为7d
    // 2024-4-7  22:14-用户数据和权限信息的最大缓存时间
    public static final Long CACHE_TIMEOUT = 7 * 24 * 60 * 60 * 1000L;


}
