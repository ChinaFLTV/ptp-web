package pfp.fltv.common.model.po.manage;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;
import pfp.fltv.common.enums.Gender;
import pfp.fltv.common.enums.UserStatus;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Setting(sortOrders = Setting.SortOrder.desc)
@Document(indexName = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName(value = "user", autoResultMap = true)
public class User implements Serializable {


    @Id
    @Field(type = FieldType.Long)
    @Schema(description = "用户ID号")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    // TODO 账号生成
    @Field(type = FieldType.Text)
    @Schema(description = "用户账号")
    private String account;

    // TODO 密码加密
    @Transient
    @Schema(description = "用户密码(加密)")
    private String password;

    @Transient
    @Schema(description = "用户绑定的手机号")
    private String phone;

    @Transient
    @Schema(description = "用户邮箱")
    private String email;

    // TODO 随机生成昵称
    @Field(type = FieldType.Text, analyzer = "analysis-ik")
    @Schema(description = "用户昵称")
    private String nickname;

    @Transient
    @Schema(description = "用户真实姓名(管理员必需)")
    private String realname;

    @Schema(description = "用户性别")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Gender gender;

    @Field(type = FieldType.Text, analyzer = "analysis-ik")
    @Schema(description = "用户的个性签名")
    private String idiograph;

    @Field(type = FieldType.Text)
    @Schema(description = "用户头像(JSON)", example = "{'type':'url','uri':'&**^&%&...'}")
    private String avatar;

    @Transient
    @Schema(description = "用户个人资料背景图片(JSON)(同上)")
    private String background;

    @Field(name = "like_num", type = FieldType.Integer)
    @Schema(description = "用户被点赞数量")
    private Integer likeNum;

    @Field(name = "user_rank", type = FieldType.Double)
    @Schema(description = "用户等级")
    private Double userRank;

    @Transient
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

    @Field(name = "address_info_id", type = FieldType.Long)
    @Schema(description = "用户地址信息ID")
    private Long addressInfoId;

    @Transient
    @Schema(description = "用户绑定的其他账号")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> bindAccounts;

    @Field(type = FieldType.Double)
    @Schema(description = "用户信誉积分")
    private Double credit;

    @Schema(description = "用户当前状态")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private UserStatus status;

    @Transient
    @Schema(description = "用户其他数据配置(JSON)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String meta;

    @Field(name = "role_id", type = FieldType.Long)
    @Schema(description = "用户角色ID")
    private Long roleId;

    @Field(name = "asset_id", type = FieldType.Long)
    @Schema(description = "用户资产ID")
    private Long assetId;

    @Field(name = "create_time", type = FieldType.Date)
    @Schema(description = "用户注册时间")
    private Timestamp createTime;

    @Field(name = "update_time", type = FieldType.Date)
    @Schema(description = "用户资料修改时间")
    private Timestamp updateTime;

    @Field(name = "is_deleted", type = FieldType.Integer)
    @Schema(description = "用户是否已被删除")
    private Integer isDeleted;


}