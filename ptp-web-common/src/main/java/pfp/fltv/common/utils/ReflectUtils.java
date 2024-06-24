package pfp.fltv.common.utils;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
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
                Class<?> valueClazz;

                // 2024-4-26  17:01-对属性字段类型进行检测,分情况处理
                if (value == null) {

                    // 2024-4-26  1:14-如果该属性无效，则填充空值
                    jsonObject.put(field.getName(), null);

                } else if ("java.base".equalsIgnoreCase((valueClazz = value.getClass()).getModule().getName())) {

                    // 2024-4-26  17:04-如果是系统类(Java 9)，则直接添加为一级属性
                    jsonObject.put(field.getName(), value);

                } else if (Set.of(valueClazz.getInterfaces()).contains(Serializable.class)) {

                    // 2024-4-26  17:06-如果该属性可序列化，同样的直接添加为一级属性
                    jsonObject.put(field.getName(), value);

                } else if (valueClazz.isEnum()) {

                    // 2024-4-28  22:30-如果字段值是枚举类型，则直接返回其字面值
                    jsonObject.put(field.getName(), value);

                } else {

                    // 2024-4-28  22:34-如果字段值是接口类型，由于接口可能存在公共类型的静态常量，因此还需要递归遍历获取(这种情况下的字段遍历是安全的)
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


    /**
     * @param obj                   原类实例
     * @param isRetainNullableField 是否保留字段值为null的字段
     * @return 转换后的Map
     * @author Lenovo/LiGuanda
     * @date 2024/6/24 PM 3:45:58
     * @version 1.0.0
     * @description 将类实例转换为Map(该方法不能去除未手动赋值而自动采用默认值的字段, 如果你想去除这种字段, 你需要提前将其手动置为null)
     * @filename ReflectUtils.java
     */
    public static Map<String, Object> bean2Map(@Nonnull Object obj, @Nonnull Boolean isRetainNullableField) {

        Map<String, Object> result = new HashMap<>();

        Class<?> clazz = obj.getClass();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {

            try {

                field.setAccessible(true);
                Object fieldValue = field.get(obj);
                Class<?> valueClazz;

                // 2024-6-24  16:44-copy from toJSONObjectForcibly方法 , 嘿嘿
                // 2024-6-24  16:45-对属性字段类型进行检测,分情况处理
                if (fieldValue == null) {

                    // 2024-6-24  16:48-根据用户选择来决定要不要存储字段值为null的字段
                    if (isRetainNullableField) {

                        // 2024-6-24  16:45-如果该属性无效，则填充空值
                        result.put(field.getName(), null);

                    }

                } else if ("java.base".equalsIgnoreCase((valueClazz = fieldValue.getClass()).getModule().getName())) {

                    // 2024-6-24  16:45-如果是系统类(Java 9)，则直接添加为一级属性
                    result.put(field.getName(), fieldValue);

                } else if (Set.of(valueClazz.getInterfaces()).contains(Serializable.class)) {

                    // 2024-6-24  16:45-如果该属性可序列化，同样的直接添加为一级属性
                    result.put(field.getName(), fieldValue);

                } else if (valueClazz.isEnum()) {

                    // 2024-6-24  16:45-如果字段值是枚举类型，则直接返回其字面值
                    result.put(field.getName(), fieldValue);

                } else {

                    // 2024-6-24  16:45-如果字段值是接口类型，由于接口可能存在公共类型的静态常量，因此还需要递归遍历获取(这种情况下的字段遍历是安全的)
                    // 2024-6-24  16:45-如果属性是不可序列化类，此时直接添加返回会丢失对象的全部属性，因此需要递归处理
                    result.put(field.getName(), bean2Map(fieldValue, isRetainNullableField));

                }

            } catch (Exception ignore) {

                // 2024-6-24  16:22-本轮字段获取失败就跳过

            }

        }

        return result;

    }


}