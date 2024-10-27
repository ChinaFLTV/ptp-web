package pfp.fltv.common.model.po.manage;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;
import pfp.fltv.common.enums.Gender;
import pfp.fltv.common.enums.UserStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Setting(sortOrders = Setting.SortOrder.desc)
@Document(indexName = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@TableName(value = "user", autoResultMap = true)
public class User implements Serializable {


    @Id
    @Field(type = FieldType.Constant_Keyword)
    @Schema(description = "用户ID号")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    // TODO 账号生成
    @Field(type = FieldType.Constant_Keyword)
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

    @Field(type = FieldType.Keyword)
    @Schema(description = "用户性别")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Gender gender = Gender.SECRET;

    @Field(name = "age", type = FieldType.Integer)
    @Schema(description = "用户年龄")
    private Integer age;

    @Field(type = FieldType.Text, analyzer = "analysis-ik")
    @Schema(description = "用户的个性签名")
    private String idiograph;

    @Field(type = FieldType.Keyword)// 2024-4-15  22:46-这里将该字段类型设置为Keyword是为了避免ElasticSearch对其进行分词而产生不必要且没有意义的分词开销
    @Schema(description = "用户头像(JSON)", example = "{'type':'url','uri':'&**^&%&...'}")
    private String avatar;

    @Transient
    @Schema(description = "用户个人资料背景图片(JSON)(同上)")
    private String background;

    @Field(name = "like_num", type = FieldType.Integer)
    @Schema(description = "用户被点赞数量")
    private Integer likeNum = 0;

    @Field(name = "share_num", type = FieldType.Integer)
    @Schema(description = "用户被转发的数量")
    private Integer shareNum = 0; // 2024-10-12  20:18-增加转发量字段

    @Field(name = "user_rank", type = FieldType.Double)
    @Schema(description = "用户等级")
    private Double userRank = 0D;

    @Transient
    @Schema(description = "用户出生年月")
    private LocalDateTime birthDate;

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

    @Field(name = "address", type = FieldType.Text, analyzer = "analysis-ik")
    @Schema(description = "用户家庭地址信息")
    private String address;

    @Transient
    @Schema(description = "用户绑定的其他账号")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> bindAccounts = new ArrayList<>();

    @Field(type = FieldType.Double)
    @Schema(description = "用户信誉积分")
    private Double credit = 100D;

    @Field(type = FieldType.Keyword)
    @Schema(description = "用户当前状态")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private UserStatus status = UserStatus.NORMAL;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Transient
    @Schema(description = "用户其他数据配置(JSON)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Map<String, Object> meta;

    @Field(name = "role_id", type = FieldType.Constant_Keyword)
    @Schema(description = "用户角色ID")
    private Long roleId;

    @Field(name = "asset_id", type = FieldType.Constant_Keyword)
    @Schema(description = "用户资产ID")
    private Long assetId;

    @Field(name = "create_time", type = FieldType.Date)
    @Schema(description = "用户注册时间")
    private LocalDateTime createTime;

    @Field(name = "update_time", type = FieldType.Date)
    @Schema(description = "用户资料修改时间")
    private LocalDateTime updateTime;

    @Field(name = "is_deleted", type = FieldType.Keyword)
    @Schema(description = "用户是否已被删除")
    private Integer isDeleted = 0;

    @Transient
    @Schema(description = "当前实体的版本(用于辅助实现乐观锁)")
    @Version
    private Integer version = 1;


}