package ptp.fltv.web.service.elasticsearch.extension;


import org.springframework.core.convert.converter.Converter;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/16 PM 8:56:01
 * @description 用于负责处理从ElasticSearch返回数据时，字段类型由{@link Date}到j{@link Timestamp}的映射
 * @filename DateToTimeStampConverter.java
 */

public class DateToTimeStampConverter implements Converter<Date, Timestamp> {


    public static final DateToTimeStampConverter INSTANCE;


    static {

        // 2024-4-16  20:59-静态内部类的形式实现单例模式
        INSTANCE = new DateToTimeStampConverter();

    }


    private DateToTimeStampConverter() {


    }


    public static DateToTimeStampConverter getInstance() {

        return INSTANCE;

    }


    @Override
    public Timestamp convert(Date source) {

        return new Timestamp(source.getTime());

    }


}