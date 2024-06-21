package ptp.fltv.web.converter;

import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pfp.fltv.common.enums.ContentRankType;
import pfp.fltv.common.exceptions.PtpException;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/21 PM 8:58:31
 * @description 解决控制器方法的参数无法由String类型转换为ContentRankType类型的问题
 * @filename String2ContentRankTypeConverter.java
 */

@Component
public class String2ContentRankTypeConverter implements Converter<String, ContentRankType> {


    @Override
    public ContentRankType convert(@Nonnull String param) {

        try {

            int code = Integer.parseInt(param);
            return ContentRankType.valueOfByCode(code);

        } catch (NumberFormatException e) {

            throw new PtpException(814, String.format("输入的ContentRankType参数非法 : %s", param));

        }

    }


}
