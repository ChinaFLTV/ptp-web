package ptp.fltv.web.handler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
    public Result<JSONObject> handlePtpException(PtpException e) {

        log.error(e.getCause() == null ? e.getLocalizedMessage() : e.getCause().getMessage());
        return Result.failure(JSON.parseObject(JSON.toJSONString(e)));

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

        log.error(e.getCause() == null ? e.getLocalizedMessage() : e.getCause().getMessage());
        return Result.failure(e.getCause() == null ? e.getLocalizedMessage() : e.getCause().getMessage());

    }


}
