package ptp.fltv.web.converter;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;
import pfp.fltv.common.enums.base.ConvertableEnum;
import pfp.fltv.common.exceptions.PtpException;

import java.util.Objects;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/8/7 PM 3:50:26
 * @description 主要将字符串类型的code同一转换为对应的ConvertableEnum的子枚举常量
 * @apiNote 问题方案来源 : <a href="https://www.cnblogs.com/shxy-wlzx/p/14280360.html">...</a>
 * @filename StringToEnumConverterFactory.java
 */

@Component
public class StringToEnumConverterFactory implements ConverterFactory<String, ConvertableEnum<?>> {


    @Nonnull
    @Override
    public <T extends ConvertableEnum<?>> Converter<String, T> getConverter(@Nonnull Class<T> targetType) {

        return new StringToEnumConverter<>(targetType);

    }


    @AllArgsConstructor
    public static class StringToEnumConverter<T extends ConvertableEnum<?>> implements Converter<String, T> {


        private Class<T> targetType;


        @Override
        public T convert(@Nonnull String source) {

            try {

                int code = Integer.parseInt(source);

                for (T convertableEnum : targetType.getEnumConstants()) {

                    if (Objects.equals(code, convertableEnum.getCode())) {

                        return convertableEnum;

                    }

                }

            } catch (Exception e) {

                throw new PtpException(815, "控制器方法入参转换出错 : java.lang.String ==> pfp.fltv.common.enums.base.ConvertableEnum(某一具体子枚举类)");

            }

            return null;

        }


    }


}