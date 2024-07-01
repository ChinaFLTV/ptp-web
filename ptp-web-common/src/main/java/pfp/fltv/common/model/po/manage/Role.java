package pfp.fltv.common.model.po.manage;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/18 上午 11:09:04
 * @description 用户角色实体类(PO)
 * @filename Role.java
 */

@TableName(value = "role", autoResultMap = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "用户角色实体类(PO)")
public class Role implements Serializable {


    @Schema(description = "角色ID")
    @TableId(type = IdType.INPUT)
    private Long id;

    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "角色所具有的权限")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> authorities = new ArrayList<>();

    @Schema(description = "角色所不被允许的权限")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> prohibition = new ArrayList<>();

    @Schema(description = "角色建立时间")
    private LocalDateTime createTime;

    @Schema(description = "(最后)修改时间")
    private LocalDateTime updateTime;

    @Schema(description = "角色是否已被删除")
    private Integer isDeleted = 0;

    @Transient
    @Schema(description = "当前实体的版本(用于辅助实现乐观锁)")
    @Version
    private Integer version = 1;


    /**
     * @return 空的角色对象
     * @author Lenovo/LiGuanda
     * @date 2024/3/31 下午 8:32:53
     * @version 1.0.0
     * @description 返回一个安全的空的角色对象
     * @filename Role.java
     */
    public static Role EMPTY_ROLE() {

        Role role = new Role();
        role.id = -1L;
        role.name = "";
        role.authorities = new ArrayList<>();
        role.prohibition = new ArrayList<>();
        role.createTime = LocalDateTime.now();
        role.updateTime = LocalDateTime.now();
        role.isDeleted = 0;

        return role;

    }


}
