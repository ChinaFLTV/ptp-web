package ptp.fltv.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/24 PM 7:04:53
 * @description WebSocket相关的自定义配置类
 * @filename WebSocketConfig.java
 */

@Configuration
public class WebSocketConfig {


    /**
     * @return 自定义的服务端端点暴露器
     * @author Lenovo/LiGuanda
     * @date 2024/6/23 PM 10:46:41
     * @version 1.0.0
     * @description 开启WebSocket功能所需实体
     * @filename WebSocketConfig.java
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {

        return new ServerEndpointExporter();

    }


}