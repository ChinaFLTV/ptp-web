package ptp.fltv.web.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/9/10 PM 10:53:32
 * @description 用于存放整个Web模块应用的配置
 * @filename AppConfig.java
 */

@Configuration
public class AppConfig {


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/9/10 PM 10:54:08
     * @version 1.0.0
     * @description 雪花ID生成器 , 主要用来生成雪花ID
     * @filename AppConfig.java
     */
    @Bean
    public Snowflake snowflake() {

        return IdUtil.getSnowflake(1, 1);

    }


}