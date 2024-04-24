package ptp.fltv.web.service.store.utils;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/24 PM 9:51:05
 * @description 基于反射原理的工具类
 * @filename ReflectUtils.java
 */

@Slf4j
public class ReflectUtils {


    /**
     * @param object  待序列化的类
     * @param options 其他配置选项(可选)
     * @return 序列化后的JSON对象
     * @author Lenovo/LiGuanda
     * @date 2024/4/24 PM 9:52:29
     * @version 1.0.0
     * @description 通过反射的方式强行将一个非序列化的类序列话为JSON对象
     * @filename ReflectUtils.java
     */
    public static JSONObject toJSONObjectForcibly(@Nonnull Object object, @Nullable Map<String, Object> options) {

        try {

            Class<?> clazz = object.getClass();

            Field[] fields = clazz.getDeclaredFields();
            JSONObject jsonObject = new JSONObject();
            for (Field field : fields) {

                field.setAccessible(true);
                Object value = field.get(object);
                jsonObject.put(field.getName(), value);

            }

            return jsonObject;

        } catch (NullPointerException | ReflectiveOperationException e) {

            log.error(e.getCause() == null ? e.getLocalizedMessage() : e.getCause().getMessage());
            return null;

        }

    }


}
