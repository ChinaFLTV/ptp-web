package ptp.fltv.web.service.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/20 PM 9:34:39
 * @description 数据存储服务的主启动类
 * @filename StoreApplication.java
 */

@EnableDiscoveryClient // 2024-4-27  20:40-开启nacos服务注册发现功能
@SpringBootApplication
public class StoreApplication {


    public static void main(String[] args) {

        SpringApplication.run(StoreApplication.class, args);

    }


}
