package ptp.fltv.web.service.elasticsearch;

import io.seata.spring.boot.autoconfigure.SeataAutoConfiguration;
import io.seata.spring.boot.autoconfigure.SeataCoreAutoConfiguration;
import io.seata.spring.boot.autoconfigure.SeataDataSourceAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/27 PM 7:50:43
 * @description ElasticSearch搜索服务的主启动类
 * @filename ElasticSearchApplication.java
 */

@EnableDiscoveryClient // 2024-4-27  20:40-开启nacos服务注册发现功能
// 2024-8-7  11:11-开发环境下 , 暂时禁用seata的自动配置(主要是在目前还没有用到seata的需求)
@SpringBootApplication(exclude = {SeataCoreAutoConfiguration.class, SeataAutoConfiguration.class, SeataDataSourceAutoConfiguration.class})
public class ElasticSearchApplication {


    // 2024-10-8  23:38-保存应用上下文引用 , 供后续不受Spring接管的实例进行配置数据的获取(比如ptp.fltv.web.service.gateway.init.SentinelDatasourceInitFunc类)
    public static ApplicationContext context;


    public static void main(String[] args) {

        context = SpringApplication.run(ElasticSearchApplication.class, args);

    }


}
