package pfp.fltv.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/18 下午 9:42:19
 * @description PTP的配置类
 * @filename PtpConfiguration.java
 */

@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(PassageProperties.class)// 2024-3-18  21:42-根据配置文件自动装填内容限制规则
public class PtpAutoConfiguration {


}
