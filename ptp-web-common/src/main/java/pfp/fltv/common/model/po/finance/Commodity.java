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
import pfp.fltv.common.enums.CommodityStatus;
import pfp.fltv.common.model.po.info.AddressInfo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/19 PM 11:02:41
 * @description 交易商品的PO
 * @filename Commodity.java
 */

@Schema(description = "商品(PO实体类)")
@Setting(sortOrders = Setting.SortOrder.desc)
@Document(indexName = "commodity")
@TableName(value = "commodity", autoResultMap = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class Commodity implements Serializable {


    @Id
    @TableId(type = IdType.ASSIGN_ID)
    @Field(type = FieldType.Constant_Keyword)
    @Schema(description = "商品唯一标识符")
    private Long id;

    @Field(type = FieldType.Constant_Keyword)
    @Schema(description = "商品卖家ID")
    private Long userId;

    @Field(type = FieldType.Text, analyzer = "analysis-ik")
    @Schema(description = "商品名称")
    private String name;

    @Field(type = FieldType.Text, analyzer = "analysis-ik")
    @Schema(description = "商品详细描述")
    private String description;

    @Transient
    @Schema(description = "标签")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> tags = new ArrayList<>();

    @Transient
    @Schema(description = "分类")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> category = new ArrayList<>();

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

    @Field(type = FieldType.Double)
    @Schema(description = "商品售价")
    private double price = 0D;

    @Field(name = "stock_quantity", type = FieldType.Integer)
    @Schema(description = "商品库存数量")
    private int stockQuantity = 1;

    // TODO 下面这几个单独抽离出一张表来，后续将进行多表联查
    @Schema(description = "商品品牌")
    private String brand;

    @Field(type = FieldType.Double)
    @Schema(description = "商品重量")
    private double weight;

    @Schema(description = "商品尺寸")
    private String size;

    @Schema(description = "商品颜色")
    private String color;

    @Schema(description = "商品材质")
    private String material;

    @Schema(description = "商品产地")
    private String origin;

    @Schema(description = "商品图片URL")
    private String imageUrl;

    @Schema(description = "商品条形码")
    private String barcode;

    @Field(type = FieldType.Keyword)
    @Schema(description = "商品状态")
    private CommodityStatus status = CommodityStatus.ON_SALE;

    @Transient
    @Schema(description = "其他数据配置(JSON)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String meta;

    @Transient
    @Schema(description = "发布时用户所在的地址信息")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private AddressInfo addressInfo;

    @Field(name = "create_time", type = FieldType.Date)
    @Schema(description = "商品发布时间")
    private Timestamp createTime;

    @Field(name = "update_time", type = FieldType.Date)
    @Schema(description = "商品信息(最后)更新时间")
    private Timestamp updateTime;

    @Field(name = "is_deleted", type = FieldType.Keyword)
    @Schema(description = "当前实体是否已被逻辑删除", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @TableLogic
    private Integer isDeleted = 0;

    // 2024-6-3  22:44-这里针对于商品版本和商品详情版本没有采用级联更新的原因是级联更新支持主键、外键字段，不支持普通字段
    // 我们同样没有选择触发器进行同事务更新是因为可能会产生循环更新的情况：如果你对commodity表单独更新的话，那你需要创建一个针对A更新的触发器；
    // 如果你对commodity_details表进行更新的话，那你需要创建一个针对B更新的触发器。至此，只有我更新其中一方，首先被更新方的触发器将被触发去更新另一个表，
    // 很好，另一个表的触发器也被触发了，因此又去更新前面的表，然后前面的表又要被更新了，然后触发器又要被触发了...如此反复，没完没了
    // 因此，我们将这两个字段的更新操作下放到业务层逻辑中。
    @Transient
    @Schema(description = "当前实体的版本(用于辅助实现乐观锁)(商品的版本号)")
    private Integer version1 = 1;

    @Transient
    @Schema(description = "当前实体的版本(用于辅助实现乐观锁)(商品详情的版本号)")
    private Integer version2 = 1;


}