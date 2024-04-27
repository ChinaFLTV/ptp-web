package ptp.fltv.web.service.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/27 PM 7:50:43
 * @description ElasticSearch搜索服务的主启动类
 * @filename ElasticSearchApplication.java
 */

@EnableDiscoveryClient // 2024-4-27  20:40-开启nacos服务注册发现功能
@SpringBootApplication
public class ElasticSearchApplication {


    public static void main(String[] args) {

        SpringApplication.run(ElasticSearchApplication.class, args);

    }


}
