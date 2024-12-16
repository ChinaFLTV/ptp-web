package com.fltv.web.service.monitor.config;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/12/16 PM 1:48:05
 * @description Docker客户端相关的个性化配置类
 * @filename DockerConfig.java
 */

@Configuration
public class DockerConfig {


    @Value("${docker.host}")
    private String host;
    @Value("${docker.api-version}")
    private String apiVersion;


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/12/16 PM 1:54:06
     * @version 1.0.0
     * @apiNote 配置Docker Desktop的2375端口可访问请参阅文档 <a href="https://blog.csdn.net/ET1131429439/article/details/126541568">...</a>和 <a href="https://blog.csdn.net/lizongti/article/details/128589967">...</a>
     * @description 用于后续与Docker容器交互的客户端(文档见 < a href = " https : / / github.com / docker - java / docker - java / blob / main / docs / getting_started.md " > ... < / a >)
     * @filename DockerConfig.java
     */
    @Bean
    public DockerClient dockerClient() {

        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(host)
                .withDockerTlsVerify(false)
                .build();

        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(60))
                .build();

        return DockerClientImpl.getInstance(config, httpClient);

    }


}