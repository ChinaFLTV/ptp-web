package ptp.fltv.web.service.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/30 PM 6:53:37
 * @description 网关服务的主启动类
 * @filename GatewayApplication.java
 */

@EnableHystrix
@EnableDiscoveryClient // 2024-5-1  21:40-开启nacos服务注册发现功能
@SpringBootApplication
public class GatewayApplication {


    public static void main(String[] args) {

        SpringApplication.run(GatewayApplication.class, args);

    }


}
