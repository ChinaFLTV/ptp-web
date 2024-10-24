package ptp.fltv.web.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/26 上午 9:44:52
 * @description MyBatisPlus配置类
 * @filename MyBatisPlusConfig.java
 */

@Configuration
public class MyBatisPlusConfig {


    /**
     * @return 自定义的MyBatisPlus拦截器
     * @author Lenovo/LiGuanda
     * @date 2024/3/26 上午 9:45:24
     * @version 1.0.0
     * @description 配置分页插件
     * @filename MyBatisPlusConfig.java
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {

        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 2024-6-3  20:55-启用MyBatisPlus的乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 2024-6-3  20:54-在使用多个插件时，请将分页插件放到插件执行链的最后面，以避免 COUNT SQL 执行不准确的问题
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));

        return interceptor;

    }


}
