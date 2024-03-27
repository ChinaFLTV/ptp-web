package ptp.fltv.web.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
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
     * @return MyBatisPlus拦截器
     * @author Lenovo/LiGuanda
     * @date 2024/3/26 上午 9:45:24
     * @version 1.0.0
     * @description 配置分页插件
     * @filename MyBatisPlusConfig.java
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {

        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;

    }


}
