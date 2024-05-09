package ptp.fltv.web.test

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

/**
 *
 * @author Lenovo/LiGuanda
 * @date 2024/5/9 PM 6:47:24
 * @version 1.0.0
 * @description 后端服务测试服务的主启动类
 * @filename TestApplication.kt
 *
 */

@EnableDiscoveryClient // 2024-5-9  18:54-开启nacos服务注册发现功能
@SpringBootApplication
open class TestApplication

fun main(args: Array<String>) {

    runApplication<TestApplication>(*args)

}
