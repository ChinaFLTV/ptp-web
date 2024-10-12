package ptp.fltv.web.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/10/10 PM 7:52:39
 * @description MongoDB相关配置
 * @filename MongoConfig.java
 */

@RequiredArgsConstructor
@Configuration
public class MongoConfig {


    // private final Integer2EnumConverterFactory integer2EnumConverterFactory;


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/10/10 PM 7:55:41
     * @version 1.0.0
     * @description 解决从MongoDB中拉取数据并转换为JavaBean的过程中报 java.lang.IllegalArgumentException: No enum constant pfp.fltv.common.model.po.ws.GroupMessage.ContentType.1801 的错误
     * @filename MongoConfig.java
     */
    /*@Bean
    public MongoCustomConversions mongoCustomConversions() {

        Converter<Integer, GroupMessage.ContentType> integer2ContentTypeConverter = integer2EnumConverterFactory.getConverter(GroupMessage.ContentType.class);
        Converter<Integer, GroupMessage.MessageType> integer2MessageTypeConverter = integer2EnumConverterFactory.getConverter(GroupMessage.MessageType.class);
        return new MongoCustomConversions(Arrays.asList(integer2ContentTypeConverter, integer2MessageTypeConverter));

    }*/


}