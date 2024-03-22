package pfp.fltv.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/18 上午 11:44:27
 * @description 用于限制内容实体(如文章标题字数等)的自动配置类
 * @filename ContentLimitAutoConfiguration.java
 */

@Data
@ConfigurationProperties(prefix = "ptp.content.limit")
public class PassageProperties {


    /**
     * 文章内容引子支持的最大长度
     */
    private Integer passageIntroductionWordsMaxLength;


}
