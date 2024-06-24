package ptp.fltv.web.service.job.handler;

import com.alibaba.csp.sentinel.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pfp.fltv.common.exceptions.PtpException;
import pfp.fltv.common.response.Result;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/19 PM 9:25:43
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
     * @date 2024/5/19 PM 10:24:21
     * @version 1.0.0
     * @description 捕获全局的PtpException异常(该异常一般由用户填写的数据非法导致的)
     * @filename GlobalExceptionHandler.java
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PtpException.class)
    public Result<String> handlePtpException(PtpException ex) {

        log.error("[{}] : {} occurred : {}", "ptp-web-service-id", "PtpException", ex.getDetailedMessage());
        Tracer.trace(ex);// 2024-5-19  22:24-上报异常信息到Sentinel

        return Result.failure(ex.getDetailedMessage());

    }


    /**
     * @param ex 产生的异常
     * @return 给前端的失败信息
     * @author Lenovo/LiGuanda
     * @date 2024/5/19 PM 9:25:56
     * @version 1.0.0
     * @description 捕获全局异常
     * @filename GlobalExceptionHandler.java
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception ex) {

        log.error("[{}] : {} occurred : {}", "ptp-web-service-id", ex.getClass().getName(), ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());
        return Result.failure(ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());

    }


}
