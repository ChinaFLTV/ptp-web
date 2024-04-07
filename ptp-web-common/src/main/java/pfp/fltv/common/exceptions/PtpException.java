package pfp.fltv.common.exceptions;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/7 下午 10:30:33
 * @description 作为应用内统一封装的异常
 * @filename PtpException.java
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PtpException extends Exception implements Serializable {


    @JsonValue
    private Integer code; // 2024-4-7  22:38-错误码为8xx
    private String message;
    private String additionalData;


    public PtpException(Integer code, String message) {

        this.code = code;
        this.message = message;

    }


}
