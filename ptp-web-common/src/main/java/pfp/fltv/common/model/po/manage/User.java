package pfp.fltv.common.model.po.manage;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfp.fltv.common.enums.Gender;
import pfp.fltv.common.enums.UserStatus;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class User implements Serializable {


    @Schema(description = "用户ID号")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    // TODO 账号生成
    @Schema(description = "用户账号")
    private String account;

    // TODO 密码加密
    @Schema(description = "用户密码(加密)")
    private String password;

    @Schema(description = "用户绑定的手机号")
    private String phone;

    @Schema(description = "用户邮箱")
    private String email;

    // TODO 随机生成昵称
    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户真实姓名(管理员必需)")
    private String realname;

    @Schema(description = "用户性别")
    private Gender gender;

    @Schema(description = "用户的个性签名")
    private String idiograph = "这个家伙很懒，什么都没写> - <";

    @Schema(description = "用户头像(JSON)", example = "{'type':'url','uri':'&**^&%&...'}")
    private String avatar;

    @Schema(description = "用户个人资料背景图片(JSON)(同上)")
    private String backgroundPicture;

    @Schema(description = "用户被点赞数量")
    private Integer likeNum;

    @Schema(description = "用户等级")
    private Double userRank;

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

    @Schema(description = "用户地址信息ID")
    private Long addressInfoId;

    @Schema(description = "用户绑定的其他账号")
    private List<String> bindAccounts;

    @Schema(description = "用户信誉积分")
    private Double credit;

    @Schema(description = "用户当前状态")
    private UserStatus status;

    @Schema(description = "用户其他数据配置(JSON)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String meta;

    @Schema(description = "用户角色ID")
    private Long roleId;

    @Schema(description = "用户资产ID")
    private Long assetId;

    @Schema(description = "用户注册时间")
    private Timestamp createTime;

    @Schema(description = "用户资料修改时间")
    private Timestamp updateTime;

    @Schema(description = "用户是否已被删除")
    private Integer isDeleted;


}