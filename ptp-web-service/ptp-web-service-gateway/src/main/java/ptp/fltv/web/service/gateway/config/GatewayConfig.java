package ptp.fltv.web.service.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ptp.fltv.web.service.gateway.filter.AuthenticationCheckFilter;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/3 PM 9:01:23
 * @description 微服务网关的配置类
 * @filename GatewayConfig.java
 */

@Configuration
public class GatewayConfig {


    /**
     * @param builder 路由规则构建器
     * @return 自定义的路由规则
     * @author Lenovo/LiGuanda
     * @date 2024/5/5 PM 9:37:40
     * @version 1.0.0
     * @description 通过代码的形式创建自定义的路由规则
     * @filename GatewayConfig.java
     */
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {

        return builder.routes()
                // 2024-5-5  21:42-放行用户对web服务的直接访问
                .route(r ->
                        r.path("/api/v1/web/**")
                                .filters(f -> f.filter(new AuthenticationCheckFilter()))
                                .metadata("response-timeout", 5000)
                                .metadata("connect-timeout", 5000)
                                .uri("http://127.0.0.1:8080")
                )
                .build();

    }


}
