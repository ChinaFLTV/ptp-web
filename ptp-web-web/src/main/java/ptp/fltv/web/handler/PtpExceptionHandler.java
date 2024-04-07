package ptp.fltv.web.handler;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pfp.fltv.common.exceptions.PtpException;
import pfp.fltv.common.response.Result;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/26 下午 7:00:14
 * @description 对业务逻辑中产生的异常进行捕获处理的控制器
 * @filename PtpExceptionHandler.java
 */

@RestControllerAdvice
public class PtpExceptionHandler {


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/4/7 下午 10:41:19
     * @version 1.0.0
     * @description 捕获全局的PtpException异常
     * @filename PtpExceptionHandler.java
     */
    @ExceptionHandler(PtpException.class)
    public Result<String> handlePtpException(PtpException e) {

        e.printStackTrace();
        return Result.failure(StringUtils.hasLength(e.getMessage()) ? e.getMessage() : e.getLocalizedMessage());

    }


    /**
     * @param e 产生的异常
     * @return 给前端的失败信息
     * @author Lenovo/LiGuanda
     * @date 2024/3/26 下午 7:10:23
     * @version 1.0.0
     * @description 捕获全局异常
     * @filename PtpExceptionHandler.java
     */
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {

        e.printStackTrace();
        return Result.failure(e.getLocalizedMessage());

    }


}
