package pfp.fltv.common.annotation;

import java.lang.annotation.*;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/16 PM 9:40:31
 * @description 记录所标注方法或类的方法的执行情况的日志
 * @filename LogRecord.java
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogRecord {



    /**
     * @author Lenovo/LiGuanda
     * @date 2024/6/16 PM 9:42:18
     * @version 1.0.0
     * @description 方法的描述
     * @filename LogRecord.java
     */
    String description() default "";


}
