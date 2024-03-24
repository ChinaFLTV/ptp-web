package pfp.fltv.common.model.po.manage;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/18 上午 11:09:04
 * @description 用户角色实体类(PO)
 * @filename Role.java
 */

@TableName("role")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "用户角色")
public class Role implements Serializable {


    @Schema(description = "角色ID")
    @TableId(type = IdType.INPUT)
    private Long id;

    @Schema(description = "角色所具有的权限")
    private List<String> authorities;

    @Schema(description = "角色所不被允许的权限")
    private List<String> prohibition;

    @Schema(description = "角色建立时间")
    private Timestamp createTime;

    @Schema(description = "(最后)修改时间")
    private Timestamp updateTime;

    @Schema(description = "角色是否已被删除")
    private Integer isDeleted;


}
