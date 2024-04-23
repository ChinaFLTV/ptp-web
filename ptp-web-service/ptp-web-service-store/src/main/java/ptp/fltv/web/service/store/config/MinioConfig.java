package ptp.fltv.web.service.store.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/20 PM 9:37:49
 * @description minio的配置类
 * @filename MinioConfig.java
 */

@Configuration
public class MinioConfig {


    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.port}")
    private Integer port;
    @Value("${minio.secure}")
    private Boolean secure;
    @Value("${minio.access-key}")
    private String accessKey;
    @Value("${minio.secret-key}")
    private String secretKey;


    /**
     * @return 自定义的minion操作客户端
     * @author Lenovo/LiGuanda
     * @date 2024/4/20 PM 10:14:06
     * @version 1.0.0
     * @description 生成自定义的minion操作客户端
     * @filename MinioConfig.java
     */
    @Bean
    public MinioClient minioClient() {

        return MinioClient.builder()
                .endpoint(endpoint, port, secure)
                .credentials(accessKey, secretKey)
                .build();

    }


}
