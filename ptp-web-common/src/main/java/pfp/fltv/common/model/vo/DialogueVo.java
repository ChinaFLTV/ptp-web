package pfp.fltv.common.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import pfp.fltv.common.enums.ContentFormat;
import pfp.fltv.common.model.po.info.AddressInfo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/18 下午 9:25:15
 * @description 客户端传过来的对话数据
 * @filename DialogueVo.java
 */

@Schema(description = "客户端传过来的对话数据")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DialogueVo implements Serializable {


    @Schema(description = "ID")
    private Long id;

    @Schema(description = "发布者ID")
    private Long uid;

    @Schema(description = "发布者昵称")
    private String nickname; // 2024-10-2  15:35-发布者昵称

    @Schema(description = "发布者头像URL")
    private String avatarUrl; // 2024-10-2  15:35-发布者头像URL

    @Schema(description = "标题", minLength = 2, maxLength = 128)
    private String title;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户填写的对话内容")
    private String content;

    @Field(type = FieldType.Keyword)
    @Schema(description = "内容形式")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ContentFormat contentFormat;

    @Schema(description = "附加的其他类型的媒体内容(JSON格式)")
    private String accessary;

    @Schema(description = "标签")
    private List<String> tags;

    @Schema(description = "分类")
    private List<String> category;

    @Schema(description = "浏览量")
    private Integer browseNum;

    @Schema(description = "点赞量")
    private Integer likeNum;

    @Schema(description = "收藏量")
    private Integer starNum; // 2024-10-2  15:42-增加收藏量字段

    @Field(name = "share_num", type = FieldType.Integer)
    @Schema(description = "转发量")
    private Integer shareNum = 0; // 2024-10-12  20:18-增加转发量字段

    @Schema(description = "发布时用户所在的地址信息")
    private AddressInfo addressInfo;

    @Schema(description = "内容创建时间")
    private LocalDateTime createTime;


}
