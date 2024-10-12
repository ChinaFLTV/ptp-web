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
 * @date 2024/10/10 PM 7:44:40
 * @description 主要将整型类型的code同一转换为对应的ConvertableEnum的子枚举常量
 * @filename Integer2EnumConverterFactory.java
 */

@Component
public class Integer2EnumConverterFactory implements ConverterFactory<Integer, ConvertableEnum<?>> {


    @Nonnull
    @Override
    public <T extends ConvertableEnum<?>> Converter<Integer, T> getConverter(@Nonnull Class<T> targetType) {

        return new IntegerToEnumConverter<>(targetType);

    }


    @AllArgsConstructor
    public static class IntegerToEnumConverter<T extends ConvertableEnum<?>> implements Converter<Integer, T> {


        private Class<T> targetType;


        @Override
        public T convert(@Nonnull Integer code) {

            try {

                for (T convertableEnum : targetType.getEnumConstants()) {

                    if (Objects.equals(code, convertableEnum.getCode())) {

                        return convertableEnum;

                    }

                }

            } catch (Exception e) {

                throw new PtpException(815, "控制器方法入参转换出错 : java.lang.Integer ==> pfp.fltv.common.enums.base.ConvertableEnum(某一具体子枚举类)");

            }

            return null;

        }


    }


}