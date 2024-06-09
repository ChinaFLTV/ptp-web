package ptp.fltv.web.annotation;

import java.lang.annotation.*;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/9 PM 11:34:27
 * @description 检测所标注方法的执行耗时
 * @filename CheckCostTime.java
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckCostTime {


}