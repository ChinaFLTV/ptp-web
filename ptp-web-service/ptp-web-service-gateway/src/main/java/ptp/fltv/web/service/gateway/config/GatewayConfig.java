package ptp.fltv.web.service.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import ptp.fltv.web.service.gateway.constants.WebConstants;
import ptp.fltv.web.service.gateway.filter.IpKeyResolver;
import ptp.fltv.web.service.gateway.handler.CircuitBreakerHandler;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/3 PM 9:01:23
 * @description 微服务网关的配置类
 * @filename GatewayConfig.java
 */

@RequiredArgsConstructor
@Configuration
public class GatewayConfig {


    private final IpKeyResolver ipKeyResolver;
    private final CircuitBreakerHandler circuitBreakerHandler;
    private final WebConstants webConstants;


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
                /*.route("ptp-web-web", r ->
                        r.order(-1)
                                .path("/api/v1/web/**")
                                .filters(f -> {

                                    f.filter(new AuthenticationCheckFilter())
                                            .requestRateLimiter(config ->

                                                    config.setKeyResolver(ipKeyResolver)
                                                            .setRateLimiter(redisRateLimiter())

                                            );

                                    return f;

                                })
                                .metadata("connect-timeout", 30_000)
                                .metadata("response-timeout", 60_000)
                                .uri("http://" + webConstants.getPTP_WEB_WEB_SERVER_HOST() + ":8080")
                )
                // 2024-5-6  21:06-禁止普通用户访问其他微服务(访问需带有内部员工凭证)(无需单独对内部微服务模块相互调用作特殊处理，因为它们之间的RPC不走微服务网关(想拦你也拦不住啊哈哈))
                .route("ptp-web-service", r ->
                        r.order(-1)
                                .host("192.168.1.12").negate().and().path("/api/v1/service/**")
                                .filters(f ->
                                        f.modifyRequestBody(String.class, String.class, MediaType.APPLICATION_JSON_VALUE,
                                                        (exchange, content) -> {

                                                            // 2024-5-6  22:31-由于Spring Cloud Gateway并没有直接提供修改请求方法的过滤器，因此，我们只能投机取巧，在这个地方进行修改
                                                            exchange.mutate().request(req -> req.method(HttpMethod.GET).build()).build();
                                                            return content == null ? Mono.empty() : Mono.just(content);

                                                        })
                                                .rewritePath("/.*", "/api/v1/web/exception/authentication/fail")
                                )
                                .metadata("connect-timeout", 30_000)
                                .metadata("response-timeout", 60_000)
                                .uri("http://" + webConstants.getPTP_WEB_SERVICE_SERVER_HOST() + ":8080")
                )*/
                .build();

    }


    /**
     * @return 自定义参数的Redis限流器
     * @author Lenovo/LiGuanda
     * @date 2024/5/9 PM 9:09:24
     * @version 1.0.0
     * @apiNote 该RedisRateLimiter的定义将会覆盖掉GatewayRedisAutoConfiguration的默认的RedisRateLimiter的定义
     * @description 根据实际运行情况产生适合的Redis限流器
     * @filename GatewayConfig.java
     */
    /*@Bean
    public RedisRateLimiter redisRateLimiter() {

        return new RedisRateLimiter(200, 1000);

    }*/


    /**
     * @return 自定义的路由功能
     * @author Lenovo/LiGuanda
     * @date 2024/5/13 PM 10:05:47
     * @version 1.0.0
     * @description 配置服务降级/熔断所需
     * @filename GatewayConfig.java
     */
    @Bean
    public RouterFunction<?> routerFunction() {

        return RouterFunctions
                .route(RequestPredicates.path("/api/v1/web/**"), circuitBreakerHandler);

    }


}
