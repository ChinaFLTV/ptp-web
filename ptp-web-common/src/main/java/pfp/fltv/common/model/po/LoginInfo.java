package pfp.fltv.common.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
*
* @author Lenovo/LiGuanda
* @date 2024/3/14 下午 9:51:27
* @description 
* @filename LoginInformation.java
* 
*/

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginInfo implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "用户ID")
    private int userID;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "用户信息")
    private User user;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "设备唯一标识码")
    private String deviceID;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "设备型号")
    private String model;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "设备MAC地址")
    private String macAddress;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "设备厂商")
    private String manufacturer;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "是否是平板")
    private boolean isTablet;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "是否是模拟器")
    private boolean isEmulator;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "设备ABIs")
    private String[] ABIs;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "设备AndroidID")
    private String AndroidID;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "设备是否rooted")
    private boolean isRooted;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "判断设备 ADB 是否可用")
    private boolean isAdbEnabled;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "获取设备系统版本号")
    private String SDKVersionName;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "获取设备系统版本码")
    private int SDKVersionCode;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "当前应用内部版本号")
    private int versionIID;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "当前应用外部版本号")
    private String versionOID;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "登录时间")
    private String date;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户登录外网IP地址")
    private String ip;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户登录所在省份")
    private String province;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户登录所在省份ID")
    private String provinceID;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户登录所在市")
    private String city;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户登录所在市ID")
    private String cityID;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户登录所用网络服务商名称")
    private String isp;


}
