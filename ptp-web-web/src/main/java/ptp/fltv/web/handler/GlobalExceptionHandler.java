package ptp.fltv.web.handler;

import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
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
 * @date 2024/3/26 下午 7:00:14
 * @description 对业务逻辑中产生的异常进行捕获处理的控制器
 * @filename GlobalExceptionHandler.java
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * @param ex 产生的异常
     * @return 给前端的失败信息
     * @author Lenovo/LiGuanda
     * @date 2024/4/7 下午 10:41:19
     * @version 1.0.0
     * @description 捕获全局的PtpException异常(该异常一般由用户填写的数据非法导致的)
     * @filename PtpExceptionHandler.java
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PtpException.class)
    public Result<String> handlePtpException(PtpException ex) {

        log.error("[{}] : {} occurred : {} [{}]", "ptp-web-web", "Exception", ex.getDetailedMessage(), ex.getCode());
        Tracer.trace(ex);// 2024-5-17  20:29-上报异常信息到Sentinel

        return Result.failure(ex.getDetailedMessage());

    }


    /**
     * @param ex 产生的异常
     * @return 给前端的失败信息
     * @author Lenovo/LiGuanda
     * @date 2024/5/18 PM 11:00:23
     * @version 1.0.0
     * @description 捕获全局的PtpException异常(该异常一般由Sentinel在触发流控机制后抛出)
     * @filename PtpExceptionHandler.java
     */
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    @ExceptionHandler(FlowException.class)
    public Result<String> handleFlowException(FlowException ex) {

        log.error("[{}] : {} occurred : {}", "ptp-web-web", "FlowException", ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());
        Tracer.trace(ex);

        return Result.failure(ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());

    }


    /**
     * @param ex 产生的异常
     * @return 给前端的失败信息
     * @author Lenovo/LiGuanda
     * @date 2024/3/26 下午 7:10:23
     * @version 1.0.0
     * @description 捕获全局异常
     * @filename PtpExceptionHandler.java
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception ex) {

        log.error("[{}] : {} occurred : {}", "ptp-web-web", "Exception", ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());
        Tracer.trace(ex);// 2024-5-17  20:31-上报异常信息到Sentinel

        return Result.failure(ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());

    }


}
