package pfp.fltv.common.model.po.agriculture;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import pfp.fltv.common.enums.base.ConvertableEnum;
import pfp.fltv.common.model.base.system.BaseEntity;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/11/29 PM 8:45:24
 * @description 农业生产环境数据(PO数据实体)(物联网在农业中的应用创新课程设计所需数据)
 * @filename AgricultureEnvironment.java
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Accessors(chain = true)
@TableName(value = "agriculture_environment", autoResultMap = true)
public class AgricultureEnvironment extends BaseEntity {


    @Schema(description = "光照强度(单位为勒克斯(Lux) , <300时 , 为低光照强度 , 此时植物生长缓慢 ; 300-1000时 , 为中等光照强度 , 此时适合大多数植物的生长 ; >1000时 , 为高光照强度 , 对某些植物来说可能有益 , 但过于强烈的光照也可能会灼伤叶片)")
    private double light; // 2024-11-29  20:47-光照强度(单位为勒克斯(Lux) , <300时 , 为低光照强度 , 此时植物生长缓慢 ; 300-1000时 , 为中等光照强度 , 此时适合大多数植物的生长 ; >1000时 , 为高光照强度 , 对某些植物来说可能有益 , 但过于强烈的光照也可能会灼伤叶片)

    @Schema(description = "二氧化碳浓度(单位为ppm , 百万分之一 , <350时 , 植物的光合作用效率较低 , 将会影响植物的生长 ; 350-1000时 , 这是大多数植物进行光合作用的合理范围 ; >1000时 , 过高的二氧化碳浓度可能会导致植物出现生理问题)")
    private double co2Concentration; // 2024-11-29  20:47-二氧化碳浓度(单位为ppm , 百万分之一 , <350时 , 植物的光合作用效率较低 , 将会影响植物的生长 ; 350-1000时 , 这是大多数植物进行光合作用的合理范围 ; >1000时 , 过高的二氧化碳浓度可能会导致植物出现生理问题)

    @Schema(description = "温度(单位为摄氏度)")
    private double temperature; // 2024-11-29  20:48-温度(单位为摄氏度)

    @Schema(description = "湿度(单位为百分比 , <30时 , 为低湿度 , 可能会导致植物水分蒸发过快 ; 40-60时 , 为适宜湿度 , 有助于植物的正常生理功能 ; >80时 , 为高湿度 , 可能会导致病害的发生)")
    private double humidity; // 2024-11-29  20:48-湿度(单位为百分比 , <30时 , 为低湿度 , 可能会导致植物水分蒸发过快 ; 40-60时 , 为适宜湿度 , 有助于植物的正常生理功能 ; >80时 , 为高湿度 , 可能会导致病害的发生)

    @Schema(description = "节点ID")
    private Long nodeId; // 2024-11-29  21:31-节点ID

    @Schema(description = "节点名称")
    private String nodeName; // 2024-11-29  21:31-节点名称

    @Schema(description = "节点状态(JSON)")
    private String nodeStatus; // 2024-11-29  20:48-节点状态(JSON)


    /**
     * @author Lenovo/LiGuanda
     * @version 1.0.0
     * @date 2024/11/29 PM 10:08:14
     * @description 专门针对于农业生产环境数据的排序规则枚举类
     * @filename AgricultureEnvironment.java
     */
    @AllArgsConstructor
    @Getter
    public enum SortType implements ConvertableEnum<Integer> {


        LIGHT_ASC(2901, "根据光照强度排序(升序)"),
        LIGHT_DESC(2902, "根据光照强度排序(降序)"),
        CO2_CONCENTRATION_ASC(2903, "根据二氧化碳浓度排序(升序)"),
        CO2_CONCENTRATION_DESC(2904, "根据二氧化碳浓度排序(降序)"),
        TEMPERATURE_ASC(2905, "根据温度排序(升序)"),
        TEMPERATURE_DESC(2906, "根据温度排序(降序)"),
        HUMIDITY_ASC(2907, "根据湿度排序(升序)"),
        HUMIDITY_DESC(2908, "根据湿度排序(降序)"),
        DEFAULT_ASC(2909, "自然排序(升序)"),
        DEFAULT_DESC(2910, "自然排序(降序)");


        @JsonValue
        private final Integer code;
        private final String comment;


    }


}
