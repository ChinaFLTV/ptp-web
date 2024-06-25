package ptp.fltv.web.service.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/19 PM 9:17:36
 * @description PTP任务服务的主启动类
 * @filename JobApplication.java
 */

@EnableDiscoveryClient // 2024-5-19  21:17-开启nacos服务注册发现功能
@SpringBootApplication
public class JobApplication {


    public static void main(String[] args) {

        SpringApplication.run(JobApplication.class, args);

    }


}
