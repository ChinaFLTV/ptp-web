package ptp.fltv.web;

import io.seata.spring.boot.autoconfigure.SeataAutoConfiguration;
import io.seata.spring.boot.autoconfigure.SeataCoreAutoConfiguration;
import io.seata.spring.boot.autoconfigure.SeataDataSourceAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/16 下午 7:31:32
 * @description PTP后台系统的WEB端入口启动类
 * @filename MainApplication.java
 */

// @EnableWebSocket // 2024-6-23  22:46-开启WebSocket功能
// @EnableAspectJAutoProxy(proxyTargetClass = true)// 2024-6-10  00:01-开启对AspectJ的支持，以实现自定义的AOP切面
@EnableDiscoveryClient // 2024-4-27  20:52-开启nacos服务注册发现功能
@ComponentScan(basePackages = "ptp.fltv")
// 2024-8-7  13:58-开发环境下 , 暂时禁用seata的自动配置(主要是在目前还没有用到seata的需求)
@SpringBootApplication(/*exclude = DataSourceAutoConfiguration.class*/exclude = {SeataCoreAutoConfiguration.class, SeataAutoConfiguration.class, SeataDataSourceAutoConfiguration.class})
@MapperScan("ptp.fltv.web.mapper")
public class WebApplication {


    // 2024-10-9  17:38-保存应用上下文引用 , 供后续不受Spring接管的实例进行配置数据的获取(比如ptp.fltv.web.service.gateway.init.SentinelDatasourceInitFunc类)
    public static ApplicationContext context;


    public static void main(String[] args) {

        context = SpringApplication.run(WebApplication.class, args);

    }


}