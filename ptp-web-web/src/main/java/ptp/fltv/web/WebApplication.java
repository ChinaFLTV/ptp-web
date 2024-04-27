package ptp.fltv.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/16 下午 7:31:32
 * @description PTP后台系统的WEB端入口启动类
 * @filename MainApplication.java
 */

@EnableDiscoveryClient // 2024-4-27  20:52-开启nacos服务注册发现功能
@ComponentScan(basePackages = "ptp.fltv")
@SpringBootApplication(/*exclude = DataSourceAutoConfiguration.class*/)
@MapperScan("ptp.fltv.web.mapper")
public class WebApplication {


    public static void main(String[] args) {

        SpringApplication.run(WebApplication.class, args);

    }


}
