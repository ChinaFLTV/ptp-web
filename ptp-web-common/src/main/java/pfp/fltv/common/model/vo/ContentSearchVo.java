package pfp.fltv.common.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfp.fltv.common.enums.ContentSearchType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/14 下午 8:27:15
 * @description 前端传递过来的根据关键词查询内容数据的ViewObject
 * @filename ContentSearchVo.java
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentSearchVo implements Serializable {


    @Schema(description = "搜索类型")
    private ContentSearchType contentSearchType;
    @Schema(description = "查询的关键词")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> keywords;
    @Schema(description = "期望查询到的结果集开始的偏移量")
    private Integer offset;
    @Schema(description = "期望查询的内容实体的数量")
    private Integer count;
    @Schema(description = "查询的用户的ID")
    private Long userId;
    @Schema(description = "查询产生的时间")
    private LocalDateTime createTime = LocalDateTime.now();


}
