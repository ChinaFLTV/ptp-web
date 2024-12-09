package com.fltv.web.service.monitor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/12/9 PM 1:55:43
 * @description PTP监控服务的主启动类
 * @filename com.fltv.web.service.monitor.MonitorApplication.java
 */

@MapperScan("com.fltv.web.service.monitor.mapper")
@EnableDiscoveryClient // 2024-12-9  13:57-开启nacos服务注册发现功能
@SpringBootApplication
public class MonitorApplication {


    // 2024-12-9  14:14-保存应用上下文引用 , 供后续不受Spring接管的实例进行配置数据的获取(比如ptp.fltv.web.service.gateway.init.SentinelDatasourceInitFunc类)
    public static ApplicationContext context;


    public static void main(String[] args) {

        context = SpringApplication.run(MonitorApplication.class, args);

    }


}