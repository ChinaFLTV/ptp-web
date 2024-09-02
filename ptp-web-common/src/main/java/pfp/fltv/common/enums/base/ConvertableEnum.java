package pfp.fltv.common.enums.base;

/**
 * @param <T> 实际对应入参的参数类型
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/9/2 PM 10:08:07
 * @description 该接口表示枚举类可由String进行转换 , 主要便于统一Spring参数解析
 * @filename ConvertableEnum.java
 */

public interface ConvertableEnum<T> {


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/9/2 PM 10:10:40
     * @version 1.0.0
     * @description 每个期望被转换的枚举常量都必须实现该方法 , 因为后序反序列化需要获取实际的Code
     * @filename ConvertableEnum.java
     */
    Integer getCode();


}