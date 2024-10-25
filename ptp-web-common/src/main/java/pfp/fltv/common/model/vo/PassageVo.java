package pfp.fltv.common.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import pfp.fltv.common.model.po.info.AddressInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/18 上午 11:37:53
 * @description 前端传输过来的Passage的部分数据
 * @filename PassageVo.java
 */

@Schema(description = "前端传输过来的Passage的部分数据")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassageVo implements Serializable {


    @Schema(description = "文章ID")
    private Long id;

    @Schema(description = "发布者ID")
    private Long uid;

    @Schema(description = "评分记录统计ID(建议在创建文章记录时 , 同步新增对应的评分统计记录)")
    private Long rateId; // 2024-10-25  20:07-增加 评分记录统计ID 字段(建议在创建文章记录时 , 同步新增对应的评分统计记录)

    @Schema(description = "文章发布者昵称")
    private String publisherNickname; // 2024-10-21  21:33-增加 文章发布者昵称 字段

    @Schema(description = "文章发布者头像URL")
    private String publisherAvatarUrl; // 2024-10-21  21:33-增加 文章发布者头像URL 字段

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户填写的文章标题")
    private String title;

    @Schema(description = "文章内容引子需要单独发送过来，因为用户那可能以上传文件/远程URL的形式发布文章，服务端这边不方便处理", maxLength = 35)
    private String introduction;

    @Schema(description = "文章封面图片资源URL")
    private String coverImgUrl; // 2024-10-21  19:21-增加 文章封面图片资源URL 字段

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "用户填写的文章内容")
    private String content;

    @Schema(description = "附加的其他类型的媒体内容(JSON格式)")
    private String accessary;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "文章标签")
    private List<String> tags;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "文章分类")
    private List<String> category;

    @Schema(description = "浏览量")
    private Integer browseNum = 0;

    @Schema(description = "点赞量")
    private Integer likeNum = 0;

    @Schema(description = "倒赞量")
    private Integer unlikeNum = 0;

    @Schema(description = "评论量")
    private Integer commentNum = 0;

    @Schema(description = "收藏量")
    private Integer starNum = 0;

    @Field(name = "share_num", type = FieldType.Integer)
    @Schema(description = "转发量")
    private Integer shareNum = 0; // 2024-10-12  20:19-增加转发量字段

    @Schema(description = "发布时用户所在的地址信息")
    private AddressInfo addressInfo;


}
