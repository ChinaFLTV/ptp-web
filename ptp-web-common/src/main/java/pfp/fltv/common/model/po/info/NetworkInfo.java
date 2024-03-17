package pfp.fltv.common.model.po.info;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/17 下午 9:34:43
 * @description 网络信息
 * @filename NetworkInfo.java
 */

@Schema(description = "网络信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NetworkInfo implements Serializable {


    @Schema(description = "公网IP地址(v4)")
    private String ipv4;

    @Schema(description = "公网IP地址(v6)")
    private String ipv6;

    @Schema(description = "网络服务商名称")
    private String isp;


}
