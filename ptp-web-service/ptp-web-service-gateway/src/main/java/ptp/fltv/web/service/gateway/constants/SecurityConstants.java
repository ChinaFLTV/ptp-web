package ptp.fltv.web.service.gateway.constants;

import java.util.*;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/4 PM 8:57:39
 * @description 存放与URL鉴权相关的配置常量
 * @filename URLConstants.java
 */

public class SecurityConstants {


    // 2024-5-4  20:58-无条件放行的路径(URL格式只能是末尾不带‘/’的路径前缀或全路径)
    public static final List<String> PERMIT_ALL_PATHS = new ArrayList<>();
    // 2024-5-4  21:26-各路径对应的所需权限
    public static final Map<String, Set<String>> URL_AUTHENTICATION_MAP = new LinkedHashMap<>();


    static {

        PERMIT_ALL_PATHS.add("/api/v1/web/gate");
        PERMIT_ALL_PATHS.add("/api/v1/web/doc.html");
        PERMIT_ALL_PATHS.add("/api/v1/web/v3/api-docs");


        URL_AUTHENTICATION_MAP.put("", Set.of());

    }


}
