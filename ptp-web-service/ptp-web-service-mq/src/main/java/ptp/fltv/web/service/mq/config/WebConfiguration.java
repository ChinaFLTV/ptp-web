package ptp.fltv.web.service.mq.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.client.RestTemplate;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/7 PM 10:51:27
 * @description Web的自定义配置类
 * @filename WebConfiguration.java
 */

@EnableAspectJAutoProxy(exposeProxy = true)// 2024-6-7  23:50-使用 AopContext.currentProxy() 解决类内方法调用引发的事务失效问题 所需配置
@Configuration
public class WebConfiguration {


    /**
     * @return 具有负载均衡功能的HTTP请求客户端
     * @author Lenovo/LiGuanda
     * @date 2024/6/7 PM 10:51:48
     * @version 1.0.0
     * @description 用于以负载均衡的方式请求服务提供者的服务(nacos建议配置)
     * @filename WebConfiguration.java
     */
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {

        return new RestTemplate();

    }


}