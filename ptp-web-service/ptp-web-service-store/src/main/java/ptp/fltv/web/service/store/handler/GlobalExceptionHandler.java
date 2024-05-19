package ptp.fltv.web.service.store.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pfp.fltv.common.response.Result;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/24 PM 9:41:57
 * @description 业务逻辑中产生的异常进行捕获处理的控制器
 * @filename GlobalExceptionHandler.java
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * @param ex 产生的异常
     * @return 给前端的失败信息
     * @author Lenovo/LiGuanda
     * @date 2024/4/24 PM 9:42:30
     * @version 1.0.0
     * @description 捕获全局异常
     * @filename GlobalExceptionHandler.java
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception ex) {

        log.error("[{}] : {} occurred : {}", "ptp-web-service-store", "Exception", ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());
        return Result.failure(ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());

    }


}
