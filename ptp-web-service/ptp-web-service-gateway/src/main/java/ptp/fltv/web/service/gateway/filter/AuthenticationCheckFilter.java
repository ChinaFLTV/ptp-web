package ptp.fltv.web.service.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import ptp.fltv.web.service.gateway.constants.SecurityConstants;
import ptp.fltv.web.service.gateway.model.ApplicationContext;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/4 PM 9:12:41
 * @description 自定义的 Spring Cloud Gateway 进行操作权限校验的局部路由过滤器
 * @filename AuthenticationCheckFilter.java
 */

@Slf4j
@Component
public class AuthenticationCheckFilter implements GatewayFilter {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 2024-5-4  21:20-这里不需要再打日志了(当然可以记录特色事件)，因为自定义的全局过滤器以及打了比较详细的日志了
        // 2024-5-4  21:23-因为这个时候请求还没打到对应web服务的某个控制器那边，因此在控制器上通过注解来表明操作权限以进行安全控制就不可能实现了
        // 2024-5-4  21:24-因此，我们需要在微服务网关这边就得提前部署好每条路径对应的权限(见 ptp.fltv.web.service.gateway.constants.URLConstants 类)
        final ServerHttpRequest request = exchange.getRequest();
        final ServerHttpResponse response = exchange.getResponse();
        final String path = request.getPath().value();

        final AtomicBoolean isHeld = new AtomicBoolean(false);
        for (Map.Entry<String, Set<String>> urlAuthoritiesEntry : SecurityConstants.URL_AUTHENTICATION_MAP.entrySet()) {

            String urlPrefix = urlAuthoritiesEntry.getKey();
            Set<String> requiredAuthorities = urlAuthoritiesEntry.getValue();

            if (path.startsWith(urlPrefix)) {

                Set<String> holdingAuthorities = new HashSet<>(ApplicationContext.ROLE.get().getAuthorities());
                // 2024-5-5  21:08-用户必须具有路径所需的全部权限才可放行
                isHeld.set(holdingAuthorities.containsAll(requiredAuthorities));
                log.info("============请求路径所要求的权限信息============");
                log.info(requiredAuthorities.toString());
                log.info("============请求用户具有的权限信息============");
                log.info(holdingAuthorities.toString());
                if (!isHeld.get()) {

                    // 2024-5-5  22:00-只要用户具有all特殊权限(但它不是首先被评估的)，则直接放行
                    if (holdingAuthorities.contains("all")) {

                        log.info("============请求用户具有特殊权限信息: all ============");
                        isHeld.set(true);
                        break;

                    } else {

                        List<String> missingAuthorities = requiredAuthorities.stream()
                                .filter(authority -> !holdingAuthorities.contains(authority))
                                .toList();
                        log.warn("============请求用户缺失的权限信息============");
                        log.warn(missingAuthorities.toString());

                    }

                }
                break;

            }

        }

        if (isHeld.get()) {

            log.info("----> 请求用户满足要求的权限，请求将被放行");
            // 2024-5-5  21:16-权限满足，放行
            return chain.filter(exchange);

        }

        // 2024-5-5  21:30-进行到这里，说明当前请求所具有的权限不满足所请求的路径要求的权限或者请求了一个非法路径(路径没有添加权限规则)
        log.warn("----> 请求用户不满足要求的权限，请求将被拒绝");
        // 2024-5-5  21:10-同样地，这里被拦截返回了依然需要清理上下文信息
        ApplicationContext.clear();
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();

    }


}
