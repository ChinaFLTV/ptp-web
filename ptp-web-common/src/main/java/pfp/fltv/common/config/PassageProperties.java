package pfp.fltv.common.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/18 上午 11:44:27
 * @description 用于限制内容实体(如文章标题字数等)的自动配置类
 * @filename ContentLimitAutoConfiguration.java
 */

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "ptp.content.limit")
@Component
public class PassageProperties {


    private Integer passageIntroductionWordsMaxLength;


}
