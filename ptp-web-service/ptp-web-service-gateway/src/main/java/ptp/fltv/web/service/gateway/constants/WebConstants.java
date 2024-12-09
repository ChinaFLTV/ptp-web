package ptp.fltv.web.service.gateway.constants;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/4 PM 9:38:47
 * @description 存放与Web相关的配置常量
 * @filename WebConstants.java
 */

@Data // 2024-11-10  00:54-将注解的代理模式调整为NO以解决类字段的@Value失效的问题
// 2024-11-9  23:23-实现Nacos动态推送新配置所需的注解
@RefreshScope/*(proxyMode = ScopedProxyMode.NO)*/ //// 2024-11-10  00:32-将注解的代理模式调整为NO以解决类字段的@Value失效的问题
@Component
public class WebConstants {


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/5/4 PM 9:39:33
     * @version 1.0.0
     * @description Web微服务模块的上下文基础路径
     * @filename WebConstants.java
     */
    public static final String WEB_CONTEXT_PATH = "/api/v1/web";

    /**
     * @author Lenovo/LiGuanda
     * @date 2024/8/4 PM 9:54:16
     * @version 1.0.0
     * @description 网关微服务模块的上下文基础路径
     * @filename WebConstants.java
     */
    public static final String GATEWAY_CONTEXT_PATH = "/api/v1/service/gateway";

    /**
     * @author Lenovo/LiGuanda
     * @date 2024/12/9 PM 3:23:21
     * @version 1.0.0
     * @description 监控微服务模块的上下文基础路径
     * @filename WebConstants.java
     */
    public static final String MONITOR_CONTEXT_PATH = "/api/v1/service/monitor";

    @Value("${ip.physical.ptp-web-web-server-host:127.0.0.1}")
    private String PTP_WEB_WEB_SERVER_HOST; // 2024-11-9  21:59-PTP-WEB服务的服务器的IP地址

    @Value("${ip.physical.ptp-web-service-server-host:127.0.0.1}")
    private String PTP_WEB_SERVICE_SERVER_HOST; // 2024-11-9  21:59-PTP-SERVICE服务的服务器的IP地址


}
