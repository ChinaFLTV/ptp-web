package ptp.fltv.web.config;

import org.springframework.context.annotation.Configuration;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/21 下午 8:49:54
 * @description Druid数据库连接池的个性化配置
 * @filename DruidConfig.java
 */

@Configuration
public class DruidConfig {


    // 2024-3-21  22:06-不使用Druid的自动配置，而采用自定义DataSource将Bean手动注入容器的方式
    /*@ConfigurationProperties("spring.datasource")
    @Bean
    public DataSource druidDataSource() {

        return new DruidDataSource();

    }*/


    // 2024-3-21  22:14-配置Druid监控后台
    /*@Bean
    public ServletRegistrationBean<?> statViewServlet() {

        ServletRegistrationBean<Servlet> registrationBean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
        Map<String, String> initParams = new HashMap<>();
        // TODO 后续可登录账号将交由云端数据库动态管理
        // 2024-3-21  22:19-设置监控后台可登录的账号
        initParams.put("loginUsername", "admin");
        initParams.put("loginPassword", "123456");
        initParams.put("allow", "");// 为空或者为null表示允许所有访问
        registrationBean.setInitParameters(initParams);
        return registrationBean;

    }*/


    // 2024-3-21  22:21-配置Druid的Web监控的过滤器
    /*@Bean
    public FilterRegistrationBean<?> webStatFilter() {


        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new WebStatFilter());

        Map<String, String> initParams = new HashMap<>();
        // 2024-3-21  22:25-表示以下请求将不在Druid监控统计范围内
        initParams.put("exclusions", "*.js,*.css,/druid/*,/jdbc/*");
        registrationBean.setInitParameters(initParams);
        // 2024-3-21  22:26-表示过滤所有请求
        registrationBean.setUrlPatterns(List.of("/*"));
        return registrationBean;

    }*/


}
