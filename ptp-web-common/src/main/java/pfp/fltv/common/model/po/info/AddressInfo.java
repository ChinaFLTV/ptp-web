package pfp.fltv.common.model.po.info;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/17 下午 9:21:32
 * @description 封装地址信息
 * @filename Address.java
 */

@Schema(description = "地址信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressInfo implements Serializable {


    @Schema(description = "海拔高度")
    private Double altitude;

    @Schema(description = "经度")
    private String longitude;

    @Schema(description = "纬度")
    private String latitude;

    @Schema(description = "所在国家")
    private String country;

    @Schema(description = "所在国家ID")
    private String countryID;

    @Schema(description = "所在省份/州")
    private String province;

    @Schema(description = "所在省份/州ID")
    private String provinceID;

    @Schema(description = "所在市")
    private String city;

    @Schema(description = "所在市ID")
    private String cityID;

    @Schema(description = "所在县")
    private String county;

    @Schema(description = "所在县ID")
    private String countyID;

    @Schema(description = "详细地址")
    private String detailedLocation;


}
