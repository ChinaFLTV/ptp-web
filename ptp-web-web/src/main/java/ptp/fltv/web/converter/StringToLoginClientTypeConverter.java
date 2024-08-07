package ptp.fltv.web.converter;

import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pfp.fltv.common.enums.LoginClientType;
import pfp.fltv.common.exceptions.PtpException;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/8/7 PM 3:50:26
 * @description 主要将字符串类型的code转换为对应的LoginClientType枚举常量
 * @filename StringToLoginClientTypeConverter.java
 */

@Component
public class StringToLoginClientTypeConverter implements Converter<String, LoginClientType> {


    @Override
    public LoginClientType convert(@Nonnull String source) {

        try {

            int code = Integer.parseInt(source);
            return LoginClientType.valueOfByCode(code);

        } catch (Exception e) {

            throw new PtpException(815, "控制器方法入参转换出错 : java.lang.String ==> pfp.fltv.common.enums.LoginClientType");

        }

    }


}