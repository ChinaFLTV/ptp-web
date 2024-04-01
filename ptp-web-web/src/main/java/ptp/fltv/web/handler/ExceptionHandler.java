package ptp.fltv.web.handler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import pfp.fltv.common.response.Result;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/26 下午 7:00:14
 * @description 对业务逻辑中产生的异常进行捕获处理的控制器
 * @filename ExceptionHandler.java
 */

@RestControllerAdvice
public class ExceptionHandler {


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/3/26 下午 7:10:23
     * @version 1.0.0
     * @description 捕获全局异常
     * @filename ExceptionHandler.java
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public Result<Exception> error(Exception e) {

        e.printStackTrace();
        return Result.failure(e);

    }


}
