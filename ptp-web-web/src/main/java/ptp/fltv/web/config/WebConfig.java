package ptp.fltv.web.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.client.RestTemplate;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/28 PM 8:46:58
 * @description Web的个性化配置类
 * @filename WebConfig.java
 */

@EnableAspectJAutoProxy(exposeProxy = true)// 2024-5-23  22:55-使用 AopContext.currentProxy() 解决类内方法调用引发的事务失效问题 所需配置
@Configuration
public class WebConfig {


    /**
     * @return 具有负载均衡功能的HTTP请求客户端
     * @author Lenovo/LiGuanda
     * @date 2024/4/28 PM 8:47:46
     * @version 1.0.0
     * @description 用于以负载均衡的方式请求服务提供者的服务(nacos建议配置)
     * @filename WebConfig.java
     */
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {

        return new RestTemplate();

    }


}