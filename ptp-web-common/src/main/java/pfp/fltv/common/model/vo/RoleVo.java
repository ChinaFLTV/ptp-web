package pfp.fltv.common.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 8:05:51
 * @description 前端传输过来的角色的部分数据
 * @filename RoleVo.java
 */

@Schema(description = "前端传输过来的Role的部分数据")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleVo {


    @Schema(description = "角色ID")
    private Long id;

    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "角色所具有的权限")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> authorities;

    @Schema(description = "角色所不被允许的权限")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> prohibition;


}
