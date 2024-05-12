package ptp.fltv.web.test.exception

import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange

/**
 *
 * @author Lenovo/LiGuanda
 * @date 2024/5/12 PM 8:59:33
 * @version 1.0.0
 * @description 处理全局异常的控制器
 * @filename ExceptionController.kt
 *
 */

@RestController
class ExceptionController {


    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(exchange: ServerWebExchange, ex: RuntimeException): String {

        return ex.cause?.message ?: ex.localizedMessage

    }


}