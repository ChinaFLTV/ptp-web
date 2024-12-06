package pfp.fltv.common.model.po.manage;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/12/6 PM 2:14:42
 * @description 角色权限实体类(PO)
 * @filename Permission.java
 */

@TableName(value = "permission", autoResultMap = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "角色权限实体类(PO)")
public class Permission implements Serializable {


    @Schema(description = "角色权限ID")
    @TableId(type = IdType.INPUT)
    private Long id;

    @Schema(description = "角色权限所属的角色ID")
    private Long roleId;

    @Schema(description = "角色权限的内容表达式")
    private String expression;

    @Schema(description = "角色权限建立时间")
    private LocalDateTime createTime;

    @Schema(description = "(最后)修改时间")
    private LocalDateTime updateTime;

    @Schema(description = "角色权限是否已被删除")
    private Integer isDeleted = 0;

    @Transient
    @Schema(description = "当前实体的版本(用于辅助实现乐观锁)")
    @Version
    private Integer version = 1;


}