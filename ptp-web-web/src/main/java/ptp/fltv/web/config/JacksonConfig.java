package ptp.fltv.web.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 上午 8:28:00
 * @description Jackson解析配置类
 * @filename JacksonConfig.java
 */

@Configuration
public class JacksonConfig {


    /**
     * @return Jackson的自定义映射构建器
     * @author Lenovo/LiGuanda
     * @date 2024/3/27 上午 8:29:12
     * @version 1.0.0
     * @description 配置Jackson的时区
     * @filename JacksonConfig.java
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {

        return builder -> builder.timeZone(TimeZone.getDefault());

    }


}
