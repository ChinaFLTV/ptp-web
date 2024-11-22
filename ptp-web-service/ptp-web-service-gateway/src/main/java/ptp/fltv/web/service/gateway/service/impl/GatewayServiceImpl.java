package ptp.fltv.web.service.gateway.service.impl;

import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.handler.predicate.RoutePredicateFactory;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import ptp.fltv.web.service.gateway.constants.WebConstants;
import ptp.fltv.web.service.gateway.service.GatewayService;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/11/10 PM 8:58:19
 * @description 网关相关的服务接口实现
 * @filename GatewayServiceImpl.java
 */

@RequiredArgsConstructor
@Service
public class GatewayServiceImpl implements GatewayService, ApplicationEventPublisherAware {


    private final WebConstants webConstants;
    private final RouteDefinitionWriter routeDefinitionWriter;
    private ApplicationEventPublisher applicationEventPublisher;


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/11/11 AM 12:48:34
     * @version 1.0.0
     * @description 初始化网关路由规则
     * @filename GatewayServiceImpl.java
     */
    @PostConstruct
    public void initGatewayRoutes() {

        routeDefinitionWriter.save(Mono.just(createWebWebRoute())).subscribe();
        routeDefinitionWriter.save(Mono.just(createWebServiceRoute())).subscribe();
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(routeDefinitionWriter));

    }


    /**
     * @return 全部可用的路由规则
     * @author Lenovo/LiGuanda
     * @date 2024/11/11 AM 12:48:47
     * @version 1.0.0
     * @description 加载(查询)全部可用的路由规则
     * @filename GatewayServiceImpl.java
     */
    @Override
    public List<RouteDefinition> loadAllRouteDefinitions() {

        List<RouteDefinition> routeDefinitions = new ArrayList<>();
        routeDefinitions.add(createWebWebRoute());
        routeDefinitions.add(createWebServiceRoute());
        return routeDefinitions;

    }


    /**
     * @return 针对于ptp-web-web微服务模块的网关规则
     * @author Lenovo/LiGuanda
     * @date 2024/11/11 AM 12:49:20
     * @version 1.0.0
     * @description 创建针对于ptp-web-web微服务模块的网关规则
     * @filename GatewayServiceImpl.java
     */
    private RouteDefinition createWebWebRoute() {

        RouteDefinition route = new RouteDefinition();
        route.setId("ptp-web-web");
        route.setOrder(-1);
        route.setUri(URI.create("http://" + webConstants.getPTP_WEB_WEB_SERVER_HOST() + ":8080"));

        PredicateDefinition pathPredicate = new PredicateDefinition();
        pathPredicate.setName("Path");
        pathPredicate.addArg(RoutePredicateFactory.PATTERN_KEY, "/api/v1/web/**");

        FilterDefinition authCheckFilter = new FilterDefinition();
        authCheckFilter.setName("AuthenticationCheck");

        FilterDefinition rateLimiterFilter = new FilterDefinition();
        rateLimiterFilter.setName("RequestRateLimiter");
        rateLimiterFilter.addArg("key-resolver", "#{@ipKeyResolver}");
        rateLimiterFilter.addArg("rate-limiter", "#{@redisRateLimiter}");

        route.setPredicates(List.of(pathPredicate));
        route.setFilters(Arrays.asList(authCheckFilter, rateLimiterFilter));
        route.getMetadata().put("connect-timeout", 10_000);
        route.getMetadata().put("response-timeout", 30_000); // 2024-11-22  9:56-将响应超时时间由10s调整为30s以以解决客户端在部分场景下偶发 Response took longer than timeout: PT10S 的异常情况

        return route;

    }


    /**
     * @return 针对于ptp-web-service微服务模块的网关规则
     * @author Lenovo/LiGuanda
     * @date 2024/11/11 AM 12:55:40
     * @version 1.0.0
     * @description 针对于ptp-web-service微服务模块的网关规则
     * @filename GatewayServiceImpl.java
     */
    private RouteDefinition createWebServiceRoute() {

        RouteDefinition route = new RouteDefinition();
        route.setId("ptp-web-service");
        route.setOrder(-1);
        route.setUri(URI.create("http://" + webConstants.getPTP_WEB_SERVICE_SERVER_HOST() + ":8080"));

        PredicateDefinition pathPredicate = new PredicateDefinition();
        pathPredicate.setName("Path");
        pathPredicate.addArg(RoutePredicateFactory.PATTERN_KEY, "/api/v1/service/**");

        /*FilterDefinition modifyRequestBodyFilter = new FilterDefinition();
        modifyRequestBodyFilter.setName("ModifyRequestBody");
        modifyRequestBodyFilter.addArg("newContentType", MediaType.APPLICATION_JSON_VALUE);
        modifyRequestBodyFilter.addArg("inClass", "java.lang.String");
        modifyRequestBodyFilter.addArg("outClass", "java.lang.String");
        modifyRequestBodyFilter.addArg("newSupplier", "#{@modifyRequestBodyGatewayFilterFactory.apply(config -> config.setInClass(String.class).setOutClass(String.class).setContentType(MediaType.APPLICATION_JSON_VALUE))}");
*/
        FilterDefinition rewritePathFilter = new FilterDefinition();
        rewritePathFilter.setName("RewritePath");
        rewritePathFilter.addArg("regexp", "/.*");
        rewritePathFilter.addArg("replacement", "/api/v1/web/exception/authentication/fail");

        route.setPredicates(List.of(pathPredicate));
        // route.setFilters(Arrays.asList(modifyRequestBodyFilter, rewritePathFilter));
        route.setFilters(List.of(rewritePathFilter));
        route.getMetadata().put("connect-timeout", 10_000);
        route.getMetadata().put("response-timeout", 30_000); // 2024-11-22  9:57-将响应超时时间由10s调整为30s以以解决客户端在部分场景下偶发 Response took longer than timeout: PT10S 的异常情况

        return route;

    }


    @Override
    public void setApplicationEventPublisher(@Nonnull ApplicationEventPublisher applicationEventPublisher) {

        this.applicationEventPublisher = applicationEventPublisher;

    }


}