package pfp.fltv.common.model.po.manage;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfp.fltv.common.enums.Role;
import pfp.fltv.common.enums.UserStatus;
import pfp.fltv.common.model.po.info.AddressInfo;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class User implements Serializable {


    @Schema(description = "用户ID号")
    @TableId(type = IdType.ASSIGN_ID)
    private int id;

    // TODO 账号生成
    @Schema(description = "用户账号")
    private String account;

    // TODO 密码加密
    @Schema(description = "用户密码(加密)")
    private String password;

    // TODO 随机生成昵称
    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户真实姓名(管理员必需)")
    private String realName;

    @Schema(description = "用户的个性签名")
    private String idiograph = "这个家伙很懒，什么都没写> - <";

    @Schema(description = "用户头像(JSON)", example = "{'type':'url','uri':'&**^&%&...'}")
    private String avatar;

    @Schema(description = "用户个人资料背景图片(JSON)(同上)")
    private String backgroundPict;

    @Schema(description = "用户被点赞数量")
    private Integer likeNum;

    @Schema(description = "用户等级")
    private Double userRank;

    @Schema(description = "用户注册时间")
    private Timestamp registerDate;

    @Schema(description = "用户出生年月")
    private Timestamp birthDate;

    // TODO 设置用户关注的博主&被关注列表
//    @Schema(description = "用户关注的用户列表JSON串")
//    private String subscriberList;
//    @Schema(description = "用户关注博主数量")
//    private int subscribeNum;
//    @Schema(description = "用户被关注粉丝数量")
//    private int followNum;

//    @Schema(description = "用户追随的用户列表JSON串")
//    private String followerList;

    // TODO 单独设置用户收藏文章
//    @Schema(description = "用户收藏博文数量")
//    private int collectNum;
//
//    @Schema(description = "用户收藏的文章列表JSON串")
//    private String collectList;

    @Schema(description = "用户地址信息")
    private AddressInfo addressInfo;

    @Schema(description = "用户绑定的其他账号")
    private String bindAccounts;

    @Schema(description = "用户信誉积分")
    private Double credit;

    @Schema(description = "用户当前状态")
    private UserStatus status;

    @Schema(description = "用户其他数据配置(JSON)")
    private String config;

    @Schema(description = "用户角色")
    private Role role;

    // TODO 用户所有权与ROLE绑定
//    @Schema(description = "用户所有特权")
//    private String privilege;

    @Schema(description = "用户资产")
    private Asset asset;


}