package pfp.fltv.common.exceptions;

import com.alibaba.fastjson2.annotation.JSONType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfp.fltv.common.map.ExceptionMap;

import java.io.Serializable;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/7 下午 10:30:33
 * @description 作为应用内统一封装的异常
 * @filename PtpException.java
 */

@JSONType(includes = {"code", "message", "additionalData"}) // 2024-4-8  18:36-指定只序列化子类字段，不去序列化父类Exception中冗长的字段
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PtpException extends Exception implements Serializable {


    private Integer code; // 2024-4-7  22:38-错误码为8xx
    private String message;
    private Object additionalData;


    public PtpException(Integer code) {

        this.code = code;
        this.message = ExceptionMap.MAP.get(code);

    }


    public PtpException(Integer code, String message) {

        this.code = code;
        this.message = message;

    }


    /**
     * @return 详细的异常信息
     * @author Lenovo/LiGuanda
     * @date 2024/5/19 PM 10:30:25
     * @version 1.0.0
     * @description 获取详细的异常信息(非堆栈信息)
     * @filename PtpException.java
     */
    public String getDetailedMessage() {

        return getCause() == null ? getLocalizedMessage() : getCause().getMessage();

    }


}
