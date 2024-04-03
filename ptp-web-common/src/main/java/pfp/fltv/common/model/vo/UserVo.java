package pfp.fltv.common.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfp.fltv.common.enums.Gender;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 8:01:21
 * @description 前端传输过来的User的部分数据
 * @filename UserVo.java
 */

@Schema(description = "前端传输过来的User的部分数据")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVo implements Serializable {


    @Schema(description = "用户ID号")
    private Long id;

    @Schema(description = "用户账号")
    private String account;

    @Schema(description = "用户密码(加密)")
    private String password;

    @Schema(description = "用户绑定的手机号")
    private String phone;

    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户真实姓名(管理员必需)")
    private String realname;

    @Schema(description = "用户性别")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Gender gender;

    @Schema(description = "用户的个性签名")
    private String idiograph;

    @Schema(description = "用户头像(JSON)", example = "{'type':'url','uri':'&**^&%&...'}")
    private String avatar;

    @Schema(description = "用户个人资料背景图片(JSON)(同上)")
    private String background;

    @Schema(description = "用户被点赞数量")
    private Integer likeNum;

    @Schema(description = "用户等级")
    private Double userRank;

    @Schema(description = "用户出生年月")
    private Timestamp birthDate;

    @Schema(description = "用户地址信息ID")
    private Long addressInfoId;

    @Schema(description = "用户绑定的其他账号")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> bindAccounts;

    @Schema(description = "用户信誉积分")
    private Double credit;

    @Schema(description = "用户角色ID")
    private Long roleId;

    @Schema(description = "用户资产ID")
    private Long assetId;


}
