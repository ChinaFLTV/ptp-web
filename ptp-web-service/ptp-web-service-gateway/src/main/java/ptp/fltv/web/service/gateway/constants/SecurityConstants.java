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

        PERMIT_ALL_PATHS.add(String.format("%s/gate", WebConstants.WEB_CONTEXT_PATH));
        PERMIT_ALL_PATHS.add(String.format("%s/doc.html", WebConstants.WEB_CONTEXT_PATH));
        PERMIT_ALL_PATHS.add(String.format("%s/v3/api-docs", WebConstants.WEB_CONTEXT_PATH));
        PERMIT_ALL_PATHS.add(String.format("%s/test", WebConstants.WEB_CONTEXT_PATH)); // 2024-5-12  22:32-为了便于测试，test路径下的请求全部予以无条件放行
        PERMIT_ALL_PATHS.add(String.format("%s/content/user/chat/room", WebConstants.WEB_CONTEXT_PATH)); // 2024-8-23  20:38-由于WebSocket连接不方便携带Cookie等身份校验的材料 , 因此暂时无条件放行WebSocket路径上的请求


        URL_AUTHENTICATION_MAP.put(String.format("%s/content/announcement", WebConstants.WEB_CONTEXT_PATH), Set.of("content:announcement:add", "content:announcement:remove", "content:announcement:list", "content:announcement:update"));
        URL_AUTHENTICATION_MAP.put(String.format("%s/content/dialogue", WebConstants.WEB_CONTEXT_PATH), Set.of("content:dialogue:add", "content:dialogue:remove", "content:dialogue:list", "content:dialogue:update"));
        URL_AUTHENTICATION_MAP.put(String.format("%s/content/passage", WebConstants.WEB_CONTEXT_PATH), Set.of("content:passage:add", "content:passage:remove", "content:passage:list", "content:passage:update"));
        URL_AUTHENTICATION_MAP.put(String.format("%s/content/comment", WebConstants.WEB_CONTEXT_PATH), Set.of("content:passage:comment:add", "content:passage:comment:remove", "content:passage:comment:list", "content:passage:comment:update"));
        URL_AUTHENTICATION_MAP.put(String.format("%s/content/user", WebConstants.WEB_CONTEXT_PATH), Set.of("content:user:add", "content:user:remove", "content:user:list", "content:user:update"));
        URL_AUTHENTICATION_MAP.put(String.format("%s/content/user/role", WebConstants.WEB_CONTEXT_PATH), Set.of("content:user:role:add", "content:user:role:remove", "content:user:role:list", "content:user:role:update"));
        URL_AUTHENTICATION_MAP.put(String.format("%s/manage/report", WebConstants.WEB_CONTEXT_PATH), Set.of("manage:report:add", "manage:report:remove", "manage:report:list", "manage:report:update"));
        URL_AUTHENTICATION_MAP.put(String.format("%s/content/banner", WebConstants.WEB_CONTEXT_PATH), Set.of("content:banner:add", "content:banner:remove", "content:banner:list", "content:banner:update"));

    }


}
