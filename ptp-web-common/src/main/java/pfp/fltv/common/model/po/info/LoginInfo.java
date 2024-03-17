package pfp.fltv.common.model.po.info;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/14 下午 9:51:27
 * @description
 * @filename LoginInformation.java
 */

@Schema(description = "用户登录信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginInfo implements Serializable {


    @Schema(description = "用户ID")
    private int uid;

    @Schema(description = "登录时间")
    private Timestamp loginDatetime;

    @Schema(description = "登录设备信息")
    private DeviceInfo deviceInfo;

    @Schema(description = "登录地址信息")
    private AddressInfo addressInfo;

    @Schema(description = "登录网络信息")
    private NetworkInfo networkInfo;


}
