package pfp.fltv.common.model.po.finance;

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
import pfp.fltv.common.enums.RecordStatus;
import pfp.fltv.common.model.po.info.AddressInfo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/19 PM 11:22:22
 * @description 商品交易订单实体类
 * @filename TransactionRecord.java
 */

@Schema(description = "商品交易订单(PO实体类)")
@Setting(sortOrders = Setting.SortOrder.desc)
@Document(indexName = "transaction_record")
@TableName(value = "transaction_record", autoResultMap = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class TransactionRecord implements Serializable {


    @Id
    @Field(type = FieldType.Constant_Keyword)
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "ID")
    private Long id;

    @Field(type = FieldType.Constant_Keyword)
    @Schema(description = "交易者ID")
    private Long uid;

    @Field(type = FieldType.Constant_Keyword)
    @Schema(description = "商品ID")
    private Long commodityId;

    @Field(type = FieldType.Text, analyzer = "analysis-ik")
    @Schema(description = "交易备注")
    private String remark;

    @Field(type = FieldType.Integer)
    @Schema(description = "下单数量")
    private Integer count = 0;

    @Field(type = FieldType.Double)
    @Schema(description = "总价")
    private Double totalPrice = 0D;

    @Field(type = FieldType.Double)
    @Schema(description = "折扣")
    private Double discount = 0D;

    @Field(type = FieldType.Text, analyzer = "analysis-ik")
    @Schema(description = "支付方式")
    private String paymentMode;

    @Transient
    @Schema(description = "标签")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> tags = new ArrayList<>();

    @Transient
    @Schema(description = "分类")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> category = new ArrayList<>();

    // 2024-5-25  22:38-以下五个字段供内部为交易订单评级或者加急处理使用
    @Field(name = "browse_num", type = FieldType.Integer)
    @Schema(description = "浏览量")
    private Integer browseNum = 0;

    @Field(name = "like_num", type = FieldType.Integer)
    @Schema(description = "点赞量")
    private Integer likeNum = 0;

    @Field(name = "unlike_num", type = FieldType.Integer)
    @Schema(description = "倒赞量")
    private Integer unlikeNum = 0;

    @Field(name = "comment_num", type = FieldType.Integer)
    @Schema(description = "评论量")
    private Integer commentNum = 0;

    @Field(name = "star_num", type = FieldType.Integer)
    @Schema(description = "收藏量")
    private Integer starNum = 0;

    @Schema(description = "中间处理该订单的人员ID")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> processors = new ArrayList<>();

    @Transient
    @Schema(description = "发布时用户所在的地址信息")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private AddressInfo addressInfo;

    @Field(type = FieldType.Keyword)
    @Schema(description = "实例状态")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private RecordStatus status = RecordStatus.NORMAL;

    @Transient
    @Schema(description = "其他数据配置(JSON)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String meta;

    @Field(name = "create_time", type = FieldType.Date)
    @Schema(description = "流水创建时间")
    private LocalDateTime createTime;

    @Field(name = "update_time", type = FieldType.Date)
    @Schema(description = "(最后)更新时间")
    private LocalDateTime updateTime;

    @Field(name = "is_deleted", type = FieldType.Keyword)
    @Schema(description = "当前实体是否已被逻辑删除", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @TableLogic
    private Integer isDeleted = 0;

    @Transient
    @Schema(description = "当前实体的版本(用于辅助实现乐观锁)")
    @Version
    private Integer version = 1;


}