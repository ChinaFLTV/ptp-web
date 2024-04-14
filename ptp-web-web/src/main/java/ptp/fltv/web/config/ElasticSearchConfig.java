package ptp.fltv.web.config;

import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/14 下午 7:46:11
 * @description ElasticSearch配置类
 * @filename ElasticSearchConfig.java
 */

@Configuration
public class ElasticSearchConfig extends ElasticsearchConfiguration {


    @Nonnull
    @Override
    public ClientConfiguration clientConfiguration() {

        return ClientConfiguration.builder()
                .connectedTo("127.0.0.1:9200")
                .build();

    }


}
