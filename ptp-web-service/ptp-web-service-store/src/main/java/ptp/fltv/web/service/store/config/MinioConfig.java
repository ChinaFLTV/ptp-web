package ptp.fltv.web.service.store.config;

import io.minio.MinioClient;
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
                .endpoint("127.0.0.1", 9000, true)
                .credentials("ptp_minio_access_key", "ptp_minio_secret_key")
                .build();

    }


}
