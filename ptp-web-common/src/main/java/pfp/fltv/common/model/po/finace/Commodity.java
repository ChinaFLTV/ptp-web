package pfp.fltv.common.model.po.finace;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import pfp.fltv.common.enums.CommodityStatus;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/19 PM 11:02:41
 * @description 交易商品的PO
 * @filename Commodity.java
 */

@Schema(description = "商品类")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class Commodity implements Serializable {


    @Id
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "商品唯一标识符")
    private Long id;
    @Schema(description = "商品名称")
    private String name;
    @Schema(description = "商品详细描述")
    private String description;
    @Transient
    @Schema(description = "标签")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> tags;

    @Transient
    @Schema(description = "分类")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> category;

    @Field(name = "browse_num", type = FieldType.Integer)
    @Schema(description = "浏览量")
    private Integer browseNum;

    @Field(name = "like_num", type = FieldType.Integer)
    @Schema(description = "点赞量")
    private Integer likeNum;

    @Field(name = "unlike_num", type = FieldType.Integer)
    @Schema(description = "倒赞量")
    private Integer unlikeNum;

    @Field(name = "comment_num", type = FieldType.Integer)
    @Schema(description = "评论量")
    private Integer commentNum;

    @Field(name = "star_num", type = FieldType.Integer)
    @Schema(description = "收藏量")
    private Integer starNum;

    @Schema(description = "商品售价")
    private double price;

    @Schema(description = "商品库存数量")
    private int stockQuantity;

    // TODO 下面这几个单独抽离出一张表来，后续将进行多表联查
    @Schema(description = "商品品牌")
    private String brand;

    @Schema(description = "商品重量")
    private double weight;

    @Schema(description = "商品尺寸")
    private String size;

    @Schema(description = "商品颜色")
    private String color;

    @Schema(description = "商品材质")
    private String material;

    @Schema(description = "商品卖家ID")
    private Long userId;

    @Schema(description = "商品产地")
    private String origin;

    @Schema(description = "商品图片URL")
    private String imageUrl;

    @Schema(description = "商品条形码")
    private String barcode;

    @Schema(description = "商品状态")
    private CommodityStatus status;

    @Transient
    @Schema(description = "其他数据配置(JSON)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String meta;

    @Field(name = "create_time", type = FieldType.Date)
    @Schema(description = "商品发布时间")
    private Timestamp createTime;

    @Field(name = "update_time", type = FieldType.Date)
    @Schema(description = "商品信息(最后)更新时间")
    private Timestamp updateTime;

    @Field(name = "is_deleted", type = FieldType.Keyword)
    @Schema(description = "当前实体是否已被逻辑删除", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @TableLogic
    private Integer isDeleted;


}

