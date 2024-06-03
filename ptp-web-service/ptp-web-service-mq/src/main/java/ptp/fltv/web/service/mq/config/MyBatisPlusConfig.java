package ptp.fltv.web.service.mq.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/3 PM 8:51:18
 * @description MyBatisPlus的自定义配置类
 * @filename MyBatisPlusConfig.java
 */

@Configuration
public class MyBatisPlusConfig {


    /**
     * @return 自定义的MyBatisPlus拦截器
     * @author Lenovo/LiGuanda
     * @date 2024/6/3 PM 8:51:49
     * @version 1.0.0
     * @description 启动MyBatisPlus的乐观锁插件所需配置
     * @filename MyBatisPlusConfig.java
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {

        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());

        return interceptor;

    }


}