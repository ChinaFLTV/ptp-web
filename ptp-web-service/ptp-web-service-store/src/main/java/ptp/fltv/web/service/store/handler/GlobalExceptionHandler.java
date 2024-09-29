package ptp.fltv.web.service.store.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pfp.fltv.common.model.po.response.Result;

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
    // 2024-9-29  15:35-抛出异常所属的父类为PTPException , 则说明该异常的出现并非原始错误 , 而是业务出错 , 因此响应码不会置为4XX , 而改写为响应体中返回给前端去进行个性化处理
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception ex) {

        log.error("[{}] : {} occurred : {}", "ptp-web-service-store", ex.getClass().getName(), ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());
        return Result.failure(ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());

    }


}
