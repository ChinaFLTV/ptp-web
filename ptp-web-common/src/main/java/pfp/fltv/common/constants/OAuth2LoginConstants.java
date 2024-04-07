package pfp.fltv.common.constants;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/7 下午 10:12:57
 * @description 记录OAuth2登录相关的常量
 * @filename OAuth2LoginConstants.java
 */

public class OAuth2LoginConstants {


    // 2024-4-7  22:15-Github OAuth2获取用户令牌的URL
    public static final String GITHUB_ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
    // 2024-4-7  22:16-Github OAuth2获取用户信息的URL
    public static final String GITHUB_ACCESS_USER_INFO_URL = "https://api.github.com/user";
    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    // 2024-4-7  22:16-客户端在Github申请的OAuth2的Client-ID
    public static String GITHUB_CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    // 2024-4-7  22:16-客户端在Github申请的OAuth2的Client-ID相配对的Client-Secret
    public static String GITHUB_CLIENT_SECRET;


}
