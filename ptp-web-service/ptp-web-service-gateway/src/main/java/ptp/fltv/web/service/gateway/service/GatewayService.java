package ptp.fltv.web.service.gateway.service;

import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/11/10 PM 8:57:44
 * @description 网关相关的服务接口
 * @filename GatewayService.java
 */

public interface GatewayService {


    /**
     * @return 当前准备好的最新的路由规则集合
     * @author Lenovo/LiGuanda
     * @date 2024/11/10 PM 9:00:03
     * @version 1.0.0
     * @description 加载全部的路由规则数据
     * @filename GatewayService.java
     */
    List<RouteDefinition> loadAllRouteDefinitions();


}