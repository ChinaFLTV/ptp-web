package pfp.fltv.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import pfp.fltv.common.enums.ResponseStatus;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * @param <T> 包装数据的类型
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/22 下午 6:23:32
 * @description 响应数据的类型
 * @filename Result.java
 */

@Data
@Builder
public class Result<T> implements Serializable {


    @Schema(description = "响应状态")
    private ResponseStatus status;
    @Schema(description = "响应时间")
    private Timestamp time;
    @Schema(description = "响应数据")
    private T data;
    public static final Blank BLANK = Blank.INSTANCE;


    public Result(ResponseStatus status, Timestamp time, T data) {

        this.status = status;
        this.time = time;
        this.data = data;

    }


    /**
     * @return 返回的成功的空的响应数据
     * @author Lenovo/LiGuanda
     * @date 2024/3/26 上午 10:21:55
     * @version 1.0.0
     * @description 返回成功的响应数据
     * @filename Result.java
     */
    public static <U> Result<U> success() {

        return new Result<>(ResponseStatus.SUCCESS, Timestamp.from(Instant.now()), null);

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

        return new Result<>(ResponseStatus.SUCCESS, Timestamp.from(Instant.now()), data);

    }


    /**
     * @param data 响应数据
     * @author Lenovo/LiGuanda
     * @date 2024/4/28 PM 8:09:53
     * @version 1.0.0
     * @description 返回响应的数据(中性 ， 即响应成功还是失败需要看封装的具体内容)
     * @description 返回的封装好的响应完成数据
     * @filename Result.java
     */
    public static <U> Result<U> neutral(U data) {

        return new Result<>(ResponseStatus.NEUTRAL, Timestamp.from(Instant.now()), data);

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

        return new Result<>(ResponseStatus.FAIL, Timestamp.from(Instant.now()), data);

    }


    /**
     * @author Lenovo/LiGuanda
     * @version 1.0.0
     * @date 2024/4/28 PM 10:03:01
     * @description 在没有响应值的情况下使用，起到一个占位表示的作用
     * @filename Result.java
     */
    private static class Blank implements Serializable {


        /**
         * @author Lenovo/LiGuanda
         * @date 2024/4/28 PM 10:07:40
         * @version 1.0.0
         * @description Result内部也不能存在new多个Blank的情况~哼
         * @filename Result.java
         */
        public static final Blank INSTANCE = new Blank();


        private Blank() {


        }


    }


}
