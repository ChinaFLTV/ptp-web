package ptp.fltv.web.test.controller

import cn.hutool.http.HttpRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
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
        @Parameter(name = "method", required = false) @RequestParam(
            name = "method", defaultValue = "GET",
            required = false
        ) method: RequestMethod = RequestMethod.GET,
        @Parameter(name = "postbody", required = false) @RequestParam(
            name = "postbody", defaultValue = "",
            required = false
        ) postbody: String = ""

    ): Mono<Map<String, Any>> {

        val threadCount = min(max(concurrentThreadCount, 1), 512)
        val accessDuration = max(duration, 100)
        val executors = Executors.newFixedThreadPool(threadCount)

        val request = HttpRequest.of(url, StandardCharsets.UTF_8)

        /*headers.run {

            for (header in entries) {

                request.header(header.key, header.value)

            }

        }*/
        request.header(
            "authorization",
            "eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYiLCJ0eXAiOiJKV1QifQ.eyJzdWIiOiJwdHAtdXNlci1sb2dpbi1pbmZvIiwib2JqZWN0X2luZm8iOiI1IiwiaXNzIjoiSVNTIiwiaWF0IjoxNzE1NTIyNjY0LCJleHAiOjE3MTYxMjc0NjQsImp0aSI6InB0cC1vYmplY3Qtand0In0.dYA0WvO7luV3hiUcTfAalv1N0_Zcaq09ew8E0KctR74"
        )

        println("-------------------------------pressureTestByUsingConcurrentThread----------------------------------")
        println("threadCount = $threadCount")
        println("url = $url")
        println("method = $method")
        println("postbody = $postbody")
        // println(headers)

        val count = AtomicLong(0)

        /*runBlocking {

            withTimeoutOrNull(accessDuration) {



            }

        }*/

        println("准备执行")

        while (count.get() != 1000L) {

            // executors.submit {

            when (method) {

                RequestMethod.GET -> request.execute()
                RequestMethod.POST -> request.body(postbody).execute()
                else -> Thread.sleep(100)

            }

            count.incrementAndGet()
            println("执行了 ${count.get()} 次请求")

            // }

        }

        println("执行完毕~")
        executors.shutdown()

        val map = mapOf("msg" to "压力测试完毕", "time" to "${duration}ms", "压测次数" to count.get())
        return Mono.just(map)

    }


    @GetMapping("/hehe")
    fun hehe(): Mono<String> {

        return Mono.just("hehe")

    }


}