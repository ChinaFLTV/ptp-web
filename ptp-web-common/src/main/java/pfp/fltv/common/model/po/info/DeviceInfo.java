package pfp.fltv.common.model.po.info;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/17 下午 9:27:24
 * @description 设备信息
 * @filename DeviceInfo.java
 */

@Schema(description = "设备信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceInfo implements Serializable {


    @Schema(description = "设备唯一标识码")
    private String deviceID;

    @Schema(description = "设备型号")
    private String model;

    @Schema(description = "设备MAC地址")
    private String macAddress;

    @Schema(description = "设备厂商")
    private String manufacturer;

    @Schema(description = "是否是平板")
    private boolean isTablet;

    @Schema(description = "是否是模拟器")
    private boolean isEmulator;

    @Schema(description = "设备ABIs")
    private String[] ABIs;

    @Schema(description = "设备AndroidID")
    private String AndroidID;

    @Schema(description = "设备是否rooted")
    private boolean isRooted;

    @Schema(description = "判断设备 ADB 是否可用")
    private boolean isAdbEnabled;

    @Schema(description = "获取设备系统版本号")
    private String SDKVersionName;

    @Schema(description = "获取设备系统版本码")
    private int SDKVersionCode;

    @Schema(description = "当前应用内部版本号")
    private int versionIID;

    @Schema(description = "当前应用外部版本号")
    private String versionOID;


}
