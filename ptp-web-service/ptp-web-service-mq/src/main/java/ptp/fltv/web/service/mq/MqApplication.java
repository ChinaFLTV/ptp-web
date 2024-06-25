package ptp.fltv.web.service.mq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/27 PM 10:13:57
 * @description PTP消息队列服务的主启动类
 * @filename MqApplication.java
 */

@MapperScan("ptp.fltv.web.service.mq.mapper")
@EnableDiscoveryClient // 2024-5-27  22:14-开启nacos服务注册发现功能
@SpringBootApplication
public class MqApplication {


    public static void main(String[] args) {

        SpringApplication.run(MqApplication.class, args);

    }


}