package ptp.fltv.web.test.controller

import cn.hutool.http.HttpRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ptp.fltv.web.test.constants.RequestMethod
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets
import java.util.concurrent.Executors
import kotlin.math.max

/**
 *
 * @author Lenovo/LiGuanda
 * @date 2024/5/9 PM 6:54:41
 * @version 1.0.0
 * @description 进行接口并发性测试的控制器
 * @filename ConcurrentTestController.kt
 *
 */

@Tag(name = "API并发性测试接口")
@RestController
@RequestMapping("/concurrent")
class ConcurrentTestController {


    @Operation(description = "通过使用并发多线程来压测某个API接口")
    fun pressureTestByUsingConcurrentThread(

        @Parameter(name = "concurrentThreadCount", required = false) @RequestParam(
            name = "concurrentThreadCount", defaultValue = "5",
            required = false
        ) concurrentThreadCount: Int = 5,
        @Parameter(name = "duration", required = false) @RequestParam(
            name = "duration", defaultValue = "10 * 1000L",
            required = false
        ) duration: Long = 10 * 1000L,
        @Parameter(name = "url", required = true) @RequestParam(
            name = "url",
            required = true
        ) url: String,
        @Parameter(name = "method", required = false) @RequestParam(
            name = "method", defaultValue = "RequestMethod.GET",
            required = false
        ) method: RequestMethod = RequestMethod.GET,
        @Parameter(name = "headers", required = false) @RequestParam(
            name = "headers",
            required = false
        ) headers: Map<String, String>?,
        @Parameter(name = "params", required = false) @RequestParam(
            name = "params",
            required = false
        ) params: Map<String, Any?>?,
        @Parameter(name = "body", required = false) @RequestParam(
            name = "body",
            required = false
        ) body: String?

    ): Mono<String> {

        val threadCount = max(concurrentThreadCount, 1)
        val accessDuration = max(duration, 100)
        val executors = Executors.newFixedThreadPool(threadCount)

        val request = HttpRequest.of(url, StandardCharsets.UTF_8)
        headers?.run {

            for (entry in entries) {

                request.header(entry.key, entry.value)

            }

        }
        params?.run {

            request.form(params)

        }

        runBlocking {

            withTimeoutOrNull(accessDuration) {

                while (true) {

                    executors.submit {

                        when (method) {

                            RequestMethod.GET -> request.execute()
                            RequestMethod.POST -> request.body(body ?: "").execute()
                            else -> Thread.sleep(500)

                        }

                    }

                }

            }

        }

        executors.shutdown()

        return Mono.just("压力测试完毕")

    }


}