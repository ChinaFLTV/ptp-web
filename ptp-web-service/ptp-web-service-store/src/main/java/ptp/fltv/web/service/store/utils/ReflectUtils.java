package ptp.fltv.web.service.store.utils;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

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

                // 2024-4-26  17:01-对属性字段类型进行检测,分情况处理
                if (value == null) {

                    // 2024-4-26  1:14-如果该属性无效，则填充空值
                    jsonObject.put(field.getName(), null);

                } else if ("java.base".equalsIgnoreCase(value.getClass().getModule().getName())) {

                    // 2024-4-26  17:04-如果是系统类(Java 9)，则直接添加为一级属性
                    jsonObject.put(field.getName(), value);

                } else if (Set.of(value.getClass().getInterfaces()).contains(Serializable.class)) {

                    // 2024-4-26  17:06-如果该属性可序列化，同样的直接添加为一级属性
                    jsonObject.put(field.getName(), value);

                } else {

                    // 2024-4-26  17:07-如果属性是不可序列化类，此时直接添加返回会丢失对象的全部属性，因此需要递归处理
                    jsonObject.put(field.getName(), toJSONObjectForcibly(value, options));

                }

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
     * @implNote options中可提供的参数：defaultFieldNames(字符串数组，提供所给可变参数对应的默认名称，以便在其值为空时依旧能进行跟踪，其大小必须与可变参数大小一致)
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

                for (int i = 0; i < objects.length; i++) {

                    if (objects[i] != null) {

                        JSONObject subJsonObject = toJSONObjectForcibly(objects[i], options);
                        jsonObject.putAll(subJsonObject);

                    } else {

                        if (options != null && options.containsKey("defaultFieldNames") && objects.length == ((String[]) options.get("defaultFieldNames")).length) {

                            jsonObject.put(((String[]) options.get("defaultFieldNames"))[i], null);

                        }

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
