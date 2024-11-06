package pfp.fltv.common.model.po.info;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pfp.fltv.common.enums.base.ConvertableEnum;
import pfp.fltv.common.model.base.system.BaseEntity;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/11/6 PM 10:28:17
 * @description 应用更新数据包
 * @filename UpdateInfo.java
 */

@TableName(value = "update_info", autoResultMap = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UpdateInfo extends BaseEntity {


    @Schema(description = "版本号")
    private Integer versionCode;

    @Schema(description = "版本名称")
    private String versionName;

    @Schema(description = "更新说明")
    private String releaseNotes;

    @Schema(description = "文件大小")
    private Double fileSize;

    @Schema(description = "更新链接")
    private String updateUrl;

    @Schema(description = "是否强制更新")
    private Boolean isMandatory;

    @Schema(description = "是否可用")
    private Boolean isAvailable;

    @Schema(description = "最小SDK版本")
    private Integer minSdkVersion;

    @Schema(description = "最大SDK版本")
    private Integer maxSdkVersion;

    @Schema(description = "APK哈希值")
    private String apkHash;

    @Schema(description = "变更日志")
    private String changeLog;

    @Schema(description = "下载次数")
    private Integer downloadNum;

    @Schema(description = "是否忽略")
    private Boolean isIgnored;

    @Schema(description = "更新类型")
    private UpdateType updateType;


    /**
     * @author Lenovo/LiGuanda
     * @version 1.0.0
     * @date 2024/11/6 PM 10:47:25
     * @description 软件更新的类型枚举类
     * @filename UpdateInfo.java
     */
    @Getter
    @AllArgsConstructor
    public enum UpdateType implements ConvertableEnum<Integer> {


        SECURITY_PATCH(2601, "安全补丁"),
        MINOR_UPDATE(2602, "小修小补"),
        MAJOR_UPDATE(2603, "重大更新"),
        BUG_FIX(2604, "错误修复"),
        FEATURE_ADDITION(2605, "功能新增"),
        PERFORMANCE_IMPROVEMENT(2606, "性能优化"),
        BETA(2607, "测试"),
        EMERGENCY_PATCH(2608, "紧急更新"),
        UNKNOWN(2609, "未知更新");


        @EnumValue
        @JsonValue
        private final Integer code;
        private final String description;


    }


}