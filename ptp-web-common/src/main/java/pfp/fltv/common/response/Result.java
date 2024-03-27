package pfp.fltv.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfp.fltv.common.enums.ResponseStatus;

import java.io.Serializable;

/**
 * @param <T> 包装数据的类型
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/22 下午 6:23:32
 * @description 响应数据的类型
 * @filename Result.java
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result<T> implements Serializable {


    @Schema(description = "响应状态")
    private ResponseStatus status;
    @Schema(description = "响应数据")
    private T data;


    /**
     * @return 返回的成功的空的响应数据
     * @author Lenovo/LiGuanda
     * @date 2024/3/26 上午 10:21:55
     * @version 1.0.0
     * @description 返回成功的响应数据
     * @filename Result.java
     */
    public static <U> Result<U> success() {

        return new Result<>(ResponseStatus.SUCCESS, null);

    }


    /**
     * @param data 响应数据
     * @return 返回的封装好的成功响应数据
     * @author Lenovo/LiGuanda
     * @version 1.0.0
     * @date 2024/3/22 下午 6:31:10
     * @description 返回成功的响应数据
     * @filename Result.java
     */
    public static <U> Result<U> success(U data) {

        return new Result<>(ResponseStatus.SUCCESS, data);

    }


    /**
     * @param data 响应数据
     * @return 返回的封装好的失败响应数据
     * @version 1.0.0
     * @author Lenovo/LiGuanda
     * @date 2024/3/22 下午 6:30:38
     * @description 返回失败的响应数据
     * @filename Result.java
     */
    public static <U> Result<U> failure(U data) {

        return new Result<>(ResponseStatus.FAIL, data);

    }


}
