package ptp.fltv.web.service.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/4 PM 9:12:41
 * @description 自定义的 Spring Cloud Gateway 进行操作权限校验的局部路由过滤器
 * @filename AuthenticationCheckFilter.java
 */

public class AuthenticationCheckFilter implements GatewayFilter {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 2024-5-4  21:20-这里不需要再打日志了(当然可以记录特色事件)，因为自定义的全局过滤器以及打了比较详细的日志了
        // 2024-5-4  21:23-因为这个时候请求还没打到对应web服务的某个控制器那边，因此在控制器上通过注解来表明操作权限以进行安全控制就不可能实现了
        // 2024-5-4  21:24-因此，我们需要在微服务网关这边就得提前部署好每条路径对应的权限(见 ptp.fltv.web.service.gateway.constants.URLConstants 类)


        return null;

    }


}
