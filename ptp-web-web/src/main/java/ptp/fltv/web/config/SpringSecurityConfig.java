package ptp.fltv.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ptp.fltv.web.service.PtpUserDetailService;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/31 下午 7:14:53
 * @description Spring Security 配置类
 * @filename SpringSecurityConfig.java
 */

@EnableWebSecurity
@Configuration
public class SpringSecurityConfig {


    /**
     * @return 自定义的用户认证器
     * @author Lenovo/LiGuanda
     * @date 2024/3/31 下午 7:41:54
     * @version 1.0.0
     * @description 返回一个自定义的用户认证器
     * @filename SpringSecurityConfig.java
     */
    @Bean
    UserDetailsService customUserDetailsService() {

        return new PtpUserDetailService();

    }


    /**
     * @return 自定义的密码加密&验证器
     * @author Lenovo/LiGuanda
     * @date 2024/4/1 下午 9:25:55
     * @version 1.0.0
     * @description 返回一个自定义的密码加密&验证器
     * @filename SpringSecurityConfig.java
     */
    @Bean
    public static PasswordEncoder passwordEncoder() {

        return NoOpPasswordEncoder.getInstance();

    }


}
