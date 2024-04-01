package ptp.fltv.web.handler;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <b> 注意：该预处理器无法对RequestBody进行预处理 </b>
 *
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 上午 10:01:33
 * @description 对业务逻辑中传入的方法参数进行预处理的控制器
 * @filename DataPreprocessController.java
 */

@RestControllerAdvice
public class DataPreprocessHandler {


    /**
     * @param dataBinder Web数据绑定器
     * @author Lenovo/LiGuanda
     * @date 2024/3/27 上午 9:40:13
     * @version 1.0.0
     * @description 进行输入参数的预处理
     * @filename DataPreprocessController.java
     */
    @InitBinder
    public void dataPreprocessing(WebDataBinder dataBinder) {

        // 20243-27  9:36-只要是String类型,就去除字符串前后的空格
        dataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

    }


}
