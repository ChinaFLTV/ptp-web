package ptp.fltv.web.config;

import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;
import org.springframework.data.elasticsearch.core.convert.GeoConverters;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import ptp.fltv.web.extersion.DateToTimeStampConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/14 下午 7:46:11
 * @description ElasticSearch配置类
 * @filename ElasticSearchConfig.java
 */

@Configuration
@EnableElasticsearchRepositories(basePackages = "ptp.fltv.web.repository") // 2024-4-17  21:13-启用ElasticSearch存储库
public class ElasticSearchConfig extends ElasticsearchConfiguration {


    @Nonnull
    @Override
    public ClientConfiguration clientConfiguration() {

        return ClientConfiguration.builder()
                .connectedTo("127.0.0.1:9200")
                .build();

    }


    /**
     * @return ElasticSearch中可能要用到的字段类型转换器
     * @author Lenovo/LiGuanda
     * @date 2024/4/16 PM 8:58:12
     * @version 1.0.0
     * @description 配置ElasticSearch的字段类型转换器，主要添加自定义的由{@link java.util.Date}到{@link java.sql.Timestamp}的转换器
     * @filename ElasticSearchConfig.java
     */
    @Override
    @Nonnull
    @Bean
    public ElasticsearchCustomConversions elasticsearchCustomConversions() {

        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.addAll(Jsr310Converters.getConvertersToRegister());
        converters.addAll(Arrays.asList(GeoConverters.PointToMapConverter.INSTANCE,
                GeoConverters.MapToPointConverter.INSTANCE,
                GeoConverters.GeoPointToMapConverter.INSTANCE,
                GeoConverters.MapToGeoPointConverter.INSTANCE,
                GeoConverters.GeoJsonToMapConverter.INSTANCE,
                GeoConverters.MapToGeoJsonConverter.INSTANCE,
                GeoConverters.GeoJsonPointToMapConverter.INSTANCE,
                GeoConverters.MapToGeoJsonPointConverter.INSTANCE,
                GeoConverters.GeoJsonMultiPointToMapConverter.INSTANCE,
                GeoConverters.MapToGeoJsonMultiPointConverter.INSTANCE,
                GeoConverters.GeoJsonLineStringToMapConverter.INSTANCE,
                GeoConverters.MapToGeoJsonLineStringConverter.INSTANCE,
                GeoConverters.GeoJsonMultiLineStringToMapConverter.INSTANCE,
                GeoConverters.MapToGeoJsonMultiLineStringConverter.INSTANCE,
                GeoConverters.GeoJsonPolygonToMapConverter.INSTANCE,
                GeoConverters.MapToGeoJsonPolygonConverter.INSTANCE,
                GeoConverters.GeoJsonMultiPolygonToMapConverter.INSTANCE,
                GeoConverters.MapToGeoJsonMultiPolygonConverter.INSTANCE,
                GeoConverters.GeoJsonGeometryCollectionToMapConverter.INSTANCE,
                GeoConverters.MapToGeoJsonGeometryCollectionConverter.INSTANCE));
        converters.add(DateToTimeStampConverter.getInstance());

        return new ElasticsearchCustomConversions(converters);

    }


}
