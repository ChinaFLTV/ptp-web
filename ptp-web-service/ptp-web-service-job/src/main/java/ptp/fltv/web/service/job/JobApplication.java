package ptp.fltv.web.service.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

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


    // 2024-10-8  23:47-保存应用上下文引用 , 供后续不受Spring接管的实例进行配置数据的获取(比如ptp.fltv.web.service.gateway.init.SentinelDatasourceInitFunc类)
    public static ApplicationContext context;


    public static void main(String[] args) {

        context = SpringApplication.run(JobApplication.class, args);

    }


}
