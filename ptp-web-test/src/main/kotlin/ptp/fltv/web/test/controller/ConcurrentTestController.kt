package ptp.fltv.web.test.controller

import cn.hutool.http.HttpRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.*
import ptp.fltv.web.test.constants.RequestMethod
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.max
import kotlin.math.min

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
    @GetMapping("/pt")
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
        @Parameter(name = "method", required = true) @RequestParam(
            name = "method", defaultValue = "GET",
            required = true
        ) method: RequestMethod = RequestMethod.GET,
        @RequestHeader headers: HttpHeaders,
        @RequestBody body: String

    ): Mono<Map<String, Any>> {

        val threadCount = min(max(concurrentThreadCount, 1), 512)
        val accessDuration = max(duration, 100)
        val executors = Executors.newFixedThreadPool(threadCount)

        val request = HttpRequest.of(url, StandardCharsets.UTF_8)
        request.header(headers)

        println("-------------------------------pressureTestByUsingConcurrentThread----------------------------------")
        println(headers)
        println(url)
        println(method)
        println(concurrentThreadCount)

        val count = AtomicLong()

        runBlocking {

            withTimeoutOrNull(accessDuration) {

                while (true) {

                    executors.submit {

                        when (method) {

                            RequestMethod.GET -> request.execute()
                            RequestMethod.POST -> request.body(body).execute()
                            else -> Thread.sleep(500)

                        }

                        count.incrementAndGet()

                    }

                }

            }

        }

        executors.shutdown()

        val map = mapOf("msg" to "压力测试完毕", "time" to "${duration}ms", "压测次数" to count.get())
        return Mono.just(map)

    }


}