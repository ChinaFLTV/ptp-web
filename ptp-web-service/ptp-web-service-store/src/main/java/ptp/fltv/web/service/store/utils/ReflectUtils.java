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
     * @description 通过反射的方式强行将一个非序列化的类序列化为JSON对象
     * @filename ReflectUtils.java
     */
    public static JSONObject toJSONObjectForcibly(@Nonnull Object object, @Nullable Map<String, Object> options) {

        try {

            Class<?> clazz = object.getClass();
            JSONObject jsonObject = new JSONObject();

            if ("java.base".equalsIgnoreCase(object.getClass().getModule().getName())) {

                // 2024-4-25  22:12-说明该类是系统类，不能对其进行反射(Java 9 : Java Platform Module System)
                jsonObject.put(clazz.getSimpleName(), object.toString());
                return jsonObject;

            }

            Field[] fields = clazz.getDeclaredFields();
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


    /**
     * @param objects 多个待序列化的类
     * @param options 其他配置选项(可选)
     * @param isTiled 是否摊开对象的属性，所有的对象属性将同一作为JSONObject的一级属性，否则，一级属性是对象类型，二级属性是相应对象的属性
     * @return 多个非序列化类序列化后的JSON对象
     * @author Lenovo/LiGuanda
     * @date 2024/4/25 PM 8:57:36
     * @version 1.0.0
     * @description 通过反射的方式强行将多个非序列化的类序列化并融合为一个JSON对象
     * @filename ReflectUtils.java
     */
    public static JSONObject toJSONObjectForciblyInBulk(@Nullable Map<String, Object> options, boolean isTiled, @Nonnull Object... objects) {

        try {

            JSONObject jsonObject = new JSONObject();

            if (isTiled) {

                for (Object object : objects) {

                    if (object != null) {

                        JSONObject subJsonObject = toJSONObjectForcibly(object, options);
                        jsonObject.putAll(subJsonObject);

                    }

                }

            } else {

                for (Object object : objects) {

                    if (object != null) {

                        JSONObject subJsonObject = toJSONObjectForcibly(object, options);
                        jsonObject.put(object.getClass().getSimpleName(), subJsonObject);

                    }

                }

            }

            return jsonObject;

        } catch (NullPointerException e) {

            log.error(e.getCause() == null ? e.getLocalizedMessage() : e.getCause().getMessage());
            return null;

        }

    }


}
