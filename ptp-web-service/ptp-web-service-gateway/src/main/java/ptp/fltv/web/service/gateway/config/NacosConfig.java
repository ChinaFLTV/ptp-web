package ptp.fltv.web.service.gateway.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigChangeEvent;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.client.config.listener.impl.AbstractConfigChangeListener;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Configuration;
import ptp.fltv.web.service.gateway.constants.WebConstants;
import ptp.fltv.web.service.gateway.service.GatewayService;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/11/10 AM 1:48:26
 * @description Nacos的配置类
 * @filename NacosConfig.java
 */

@Slf4j
@RequiredArgsConstructor
@Configuration
public class NacosConfig implements InitializingBean, ApplicationEventPublisherAware {


    private final NacosConfigManager nacosConfigManager;
    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${spring.profiles.active}")
    private String activeProfile;
    @Value("${spring.cloud.nacos.config.file-extension}")
    private String fileExtension;
    private final RouteDefinitionWriter routeDefinitionWriter;
    private ApplicationEventPublisher applicationEventPublisher;
    private final RouteLocator routeLocator;
    private final WebConstants webConstants;
    private final GatewayService gatewayService;
    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 5, 0, TimeUnit.MINUTES, new LinkedBlockingQueue<>()); // 2024-11-11  15:38-专门用于执行网关路由规则变更任务的线程池


    @Override
    public void afterPropertiesSet() throws NacosException {

        nacosConfigManager.getConfigService()
                .addListener(applicationName + "-" + activeProfile + "." + fileExtension, "DEFAULT_GROUP", new AbstractConfigChangeListener() {


                    @Override
                    public void receiveConfigChange(ConfigChangeEvent event) {

                        // 2024-11-10  1:58-在接收到云端配置发生变更的事件之后 , 便需要重新构造路由以达到动态刷新网关路由的目的
                        // 2024-11-11  15:59-这里之所以采用线程池通过其他线程异步等待五秒后才去更新路由规则 , 是因为直接在receiveConfigChange方法中更新相当于在配置更新前去刷新规则 , 显然这个时机刷新过后的规则数据是没有发生变化的
                        // 2024-11-11  2:35-这里仅仅是做了修改单个路由的URI参数(通过先删除后添加的方式) , 因为当前的业务需求有且仅有这个
                        threadPoolExecutor.submit(() -> {

                            try {

                                Thread.sleep(5000L); // 2024-11-11  15:43-睡眠五秒以等待配置充分被更新后再进行后续的业务操作

                                List<RouteDefinition> oldRouteDefinitions = gatewayService.loadAllRouteDefinitions();
                                for (RouteDefinition oldRouteDefinition : oldRouteDefinitions) {

                                    routeDefinitionWriter.delete(Mono.just(oldRouteDefinition.getId())).subscribe();

                                    switch (oldRouteDefinition.getId()) {

                                        case "ptp-web-web" -> oldRouteDefinition.setUri(URI.create("http://" + webConstants.getPTP_WEB_WEB_SERVER_HOST() + ":8080"));

                                        // case "ptp-web-service" -> oldRouteDefinition.setUri(URI.create("http://" + webConstants.getPTP_WEB_SERVICE_SERVER_HOST() + ":8080"));

                                        case "ptp-web-service-monitor" -> oldRouteDefinition.setUri(URI.create("http://" + webConstants.getPTP_WEB_SERVICE_MONITOR_SERVER_HOST() + ":8240"));

                                    }

                                    routeDefinitionWriter.save(Mono.just(oldRouteDefinition)).subscribe();

                                }

                                applicationEventPublisher.publishEvent(new RefreshRoutesEvent(routeDefinitionWriter));

                            } catch (Exception ex) {

                                log.error("[{}] : {} occurred : {}", "ptp-web-service-gateway : " + NacosConfig.class.getCanonicalName(), ex.getClass().getName(), ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());

                            }

                        });

                    }


                });

    }


    @Override
    public void setApplicationEventPublisher(@Nonnull ApplicationEventPublisher applicationEventPublisher) {

        this.applicationEventPublisher = applicationEventPublisher;

    }


}