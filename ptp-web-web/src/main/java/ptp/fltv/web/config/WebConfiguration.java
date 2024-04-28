package ptp.fltv.web.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/28 PM 8:46:58
 * @description Web的个性化配置类
 * @filename WebConfiguration.java
 */

@Configuration
public class WebConfiguration {


    /**
     * @return 具有负载均衡功能的HTTP请求客户端
     * @author Lenovo/LiGuanda
     * @date 2024/4/28 PM 8:47:46
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
