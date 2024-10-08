package ptp.fltv.web.service.mq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

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


    // 2024-10-9  00:09-保存应用上下文引用 , 供后续不受Spring接管的实例进行配置数据的获取(比如ptp.fltv.web.service.gateway.init.SentinelDatasourceInitFunc类)
    public static ApplicationContext context;


    public static void main(String[] args) {

        context = SpringApplication.run(MqApplication.class, args);

    }


}