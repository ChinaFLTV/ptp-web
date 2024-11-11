package ptp.fltv.web.service.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/11/11 AM 1:42:46
 * @description 以代码的方式动态添加路由规则所需的过滤器配置的样式
 * @filename AuthenticationCheckGatewayFilterFactory.java
 */

@Component
public class AuthenticationCheckGatewayFilterFactory<T> extends AbstractGatewayFilterFactory<T> {


    @Override
    public GatewayFilter apply(T config) {

        return new AuthenticationCheckFilter();

    }


}