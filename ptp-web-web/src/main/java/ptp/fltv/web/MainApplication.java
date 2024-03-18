package ptp.fltv.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pfp.fltv.common.config.ContentLimitAutoConfiguration;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/16 下午 7:31:32
 * @description PTP后台系统的WEB端入口启动类
 * @filename MainApplication.java
 */


@EnableConfigurationProperties(ContentLimitAutoConfiguration.class)// 2024-3-18  11:46-根据配置文件自动装填内容限制规则
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class MainApplication {


    public static void main(String[] args) {

        SpringApplication.run(MainApplication.class, args);

    }


}
