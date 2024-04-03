package ptp.fltv.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ptp.fltv.web.service.PtpUserDetailService;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

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


    /**
     * @return 自定义的安全过滤器链
     * @author Lenovo/LiGuanda
     * @date 2024/4/2 下午 7:30:41
     * @version 1.0.0
     * @description 指定针对于全局URL的安全过滤器链
     * @filename SpringSecurityConfig.java
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/gate/login", "/doc.html", "/v3/api-docs/**", "/webjars/**", "/swagger-ui.html", "/swagger-ui/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin(withDefaults())
                .httpBasic(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));// 2024-4-2  20:50-禁用session会话
        // .cors(cors -> cors.init())
        // .exceptionHandling(exceptionHandler -> exceptionHandler.authenticationEntryPoint(new PtpAuthenticationEntryPoint()))

        return http.build();

    }


    /**
     * @return 自定义的跨域请求策略
     * @author Lenovo/LiGuanda
     * @date 2024/4/2 下午 7:59:54
     * @version 1.0.0
     * @description 集成自定义的跨域请求策略
     * @filename SpringSecurityConfig.java
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;

    }


}
