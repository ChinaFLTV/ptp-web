package ptp.fltv.web.service.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayParamFlowItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStrategy;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.support.DefaultServerCodecConfigurer;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/14 PM 9:41:15
 * @description Sentinel的自定义配置类
 * @filename SentinelConfig.java
 */

@Configuration
public class SentinelConfig {


    private final List<ViewResolver> viewResolvers;


    public SentinelConfig(ObjectProvider<List<ViewResolver>> viewResolversProvider) {

        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);

    }


    @PostConstruct
    public void doInit() {

        initSentinelGatewayRules();
        setHandler();

    }


    private static void initDegradeRule() {
        List<DegradeRule> rules = new ArrayList<>();
        DegradeRule rule = new DegradeRule("hehehe")
                .setGrade(CircuitBreakerStrategy.SLOW_REQUEST_RATIO.getType())
                // Max allowed response time
                .setCount(50)
                // Retry timeout (in second)
                .setTimeWindow(10)
                // Circuit breaker opens when slow request ratio > 60%
                .setSlowRatioThreshold(0.6)
                .setMinRequestAmount(100)
                .setStatIntervalMs(20000);
        rules.add(rule);

        DegradeRuleManager.loadRules(rules);
        System.out.println("Degrade rule loaded: " + rules);
    }


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/5/14 PM 9:52:34
     * @version 1.0.0
     * @description 根据RouteID初始化Sentinel网关规则
     * @filename SentinelConfig.java
     */
    private void initSentinelGatewayRules() {

        GatewayFlowRule gatewayFlowRule = new GatewayFlowRule("ptp-web-web")
                .setIntervalSec(1)
                .setParamItem(

                        new GatewayParamFlowItem()
                                .setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)

                );

        GatewayRuleManager.loadRules(Set.of(gatewayFlowRule));

        List<DegradeRule> degradeRules = new ArrayList<>();
        degradeRules.add(
                new DegradeRule("ptp-web-web") // 资源名称
                        .setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO) // 异常比率模式(秒级)
                        .setCount(0.5) // 异常比率阈值(50%)
                        .setMinRequestAmount(4)// 最小请求数(4个)
                        .setTimeWindow(10) // 熔断降级时间(10s)
                        .setStatIntervalMs(60_000) // 设置统计间隔(60s)
        );
        degradeRules.add(

                new DegradeRule("ptp-web-web")
                        .setGrade(RuleConstant.DEGRADE_DEFAULT_SLOW_REQUEST_AMOUNT)
                        .setMinRequestAmount(4)
                        .setCount(3000)
                        .setSlowRatioThreshold(0.5)
                        .setStatIntervalMs(60_000)
                        .setTimeWindow(10)

        );

        // 加载服务降级规则
        DegradeRuleManager.loadRules(degradeRules);

    }


    private void setHandler() {

        // 2024-5-14  23:04-解决Sentinel的BUG：
        /*
         * issue : <a href="https://github.com/alibaba/Sentinel/issues/3298">BUG</a>
         * 今天再次试了一下，发现直接用JDK17重新编译sentinel-spring-cloud-gateway-adapter-1.8.6是不行的，
         * 估计是因为DefaultBlockRequestHandler中使用到的ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)在不同版本中参数类型不一样导致的，
         * 在spring-webflux:6.0.4中ServerResponse.status(HttpStatusCode status)方法需要的是HttpStatusCode，
         * 而这里传入的是HttpStatus，所以在spring-webflux:6.0.4中ServerResponse没有找到对应的方法。
         * */
        GatewayCallbackManager.setBlockHandler((exchange, ex) -> ServerResponse.ok().body(Mono.just("限流啦,请求太频繁"), String.class));

    }


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/5/14 PM 9:42:41
     * @version 1.0.0
     * @description 为Spring Cloud Gateway注册块异常处理程序。
     * @filename SentinelConfig.java
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {

        return new SentinelGatewayBlockExceptionHandler(viewResolvers, new DefaultServerCodecConfigurer());

    }


}
