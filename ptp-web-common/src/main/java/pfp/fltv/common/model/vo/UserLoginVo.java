package pfp.fltv.common.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfp.fltv.common.model.po.info.LoginInfo;

import java.io.Serializable;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/2 下午 8:39:33
 * @description 前端传输过来的用户登录的部分数据
 * @filename LoginInfoVo.java
 */

@Schema(description = "前端传输过来的用户登录的部分数据")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginVo implements Serializable {


    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户密码")
    private String password;

    @Schema(description = "账号(也可以是ID , 该字段专门用于第三方登录)")
    private String account;

    @Schema(description = "用户登录的其他信息")
    private LoginInfo loginInfo;


}
