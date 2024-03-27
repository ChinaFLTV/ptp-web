package ptp.fltv.web.aop;

import com.alibaba.fastjson2.JSON;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pfp.fltv.common.model.vo.DialogueVo;
import pfp.fltv.common.response.Result;

import java.beans.PropertyEditorSupport;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/26 下午 7:00:14
 * @description 用于专门对业务逻辑中产生的异常进行捕获处理的类
 * @filename ExceptionHandler.java
 */

@RestControllerAdvice
public class ExceptionController {


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/3/26 下午 7:10:23
     * @version 1.0.0
     * @description 捕获全局异常
     * @filename ExceptionHandler.java
     */
    @ExceptionHandler(Exception.class)
    public Result<Exception> error(Exception e) {

        e.printStackTrace();
        return Result.failure(e);

    }


    /**
     * @param dataBinder Web数据绑定器
     * @author Lenovo/LiGuanda
     * @date 2024/3/27 上午 9:40:13
     * @version 1.0.0
     * @description 进行输入参数的预处理
     * @filename ExceptionController.java
     */
    @InitBinder
    public void dataPreprocessing(WebDataBinder dataBinder) {

        // 20243-27  9:36-只要是String类型,就去除字符串前后的空格
        dataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

    }


}
