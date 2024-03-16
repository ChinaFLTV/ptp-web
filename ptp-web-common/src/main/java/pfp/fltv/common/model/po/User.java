package pfp.fltv.common.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfp.fltv.common.enums.Role;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class User implements Serializable {


    @Schema(accessMode = Schema.AccessMode.READ_WRITE)
    private String nickname = null;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户的个性签名")
    private String idiograph = "这个家伙很懒，什么都没写> - <";

    @Schema(accessMode = Schema.AccessMode.READ_WRITE)
    private String avatar = null;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户个人资料背景图片")
    private String backgroundPict = null;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户被点赞数量")
    private int likeNum;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户头衔")
    private double userRank;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户注册时间")
    private Timestamp registerDate;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户出生年月")
    private Timestamp birthDate;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户关注博主数量")
    private int subscribeNum;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户关注的用户列表JSON串")
    private String subscriberList;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户被关注粉丝数量")
    private int followNum;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户追随的用户列表JSON串")
    private String followerList;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户收藏博文数量")
    private int collectNum;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户收藏的文章列表JSON串")
    private String collectList;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户家庭住址")
    private String address;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户绑定的手机号")
    private String bindPhoneNumber;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户绑定的QQ帐号")
    private String bindQQNumber;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户绑定的微信账号")
    private String bindWechatNumber;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户绑定的新浪微博账号")
    private String bindSinaWebNumber;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户ID号")
    @TableId(type = IdType.ASSIGN_ID)
    private int userID;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户信誉积分")
    private int creditScore;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户当前状态")
    private String status;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户密码")
    private String password;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户账号所对应的PTP账号")
    private String ptpID;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户其他数据配置")
    private String config;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户角色")
    private Role role;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户所有特权")
    private String privilege;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "当前用户的余额")
    private double balance;


}