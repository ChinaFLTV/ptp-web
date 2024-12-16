package com.fltv.web.service.monitor.constants;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/12/16 PM 3:45:36
 * @description Docker容器相关的常量类
 * @apiNote 尽量避免直接在Spring通过CGlib代理的类上使用 @RefreshScope 注解 , 这样做容易导致其他意外的情况出现 , 建议单独使用一个实体类来存储需要被动态刷新的属性
 * @filename DockerContainerConstants.java
 */

@Data // 2024-12-16  15:50-应使用get方法来访问需要被动态刷新的字段 , 直接访问字段将会得到null值
@Component
@RefreshScope
public class DockerContainerConstants {


    @Value("${docker.containers.ptp-backend-mysql-1}")
    private String ptpBackendMySql1ContainerId;


}