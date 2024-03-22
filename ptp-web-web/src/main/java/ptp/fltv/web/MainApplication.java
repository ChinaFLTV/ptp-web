package ptp.fltv.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import javax.sql.DataSource;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/16 下午 7:31:32
 * @description PTP后台系统的WEB端入口启动类
 * @filename MainApplication.java
 */


@ComponentScan("ptp.fltv")
@SpringBootApplication(/*exclude = DataSourceAutoConfiguration.class*/)
public class MainApplication {


    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(MainApplication.class, args);
        System.out.println(context.getBean(DataSource.class));

    }


}
