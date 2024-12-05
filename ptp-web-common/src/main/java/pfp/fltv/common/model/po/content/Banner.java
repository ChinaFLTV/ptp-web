package pfp.fltv.common.model.po.content;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import pfp.fltv.common.model.base.content.BaseEntity;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/10/20 AM 1:09:12
 * @description 轮播实体类(PO)
 * @filename Banner.java
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Accessors(chain = true)
@TableName(value = "banner", autoResultMap = true)
public class Banner extends BaseEntity {


    private String imgUrl; // 2024-10-20  1:20-轮播配图云端资源直链(建议比例为>16:9)
    // private ContentType contentType; // 2024-12-4  14:31-公告详细内容的类型(默认为纯文本)
    // private String contentUrl; // 2024-12-4  13:52-内容详情的访问URL(建议前端通过提供Web访问的方式来查看公告详情 , 如果公告内容类型是远程URL的话)


    /**
     * @author Lenovo/LiGuanda
     * @version 1.0.0
     * @date 2024/12/4 PM 2:27:11
     * @description 公告详细内容的类型
     * @filename Banner.java
     */
    /*@Getter
    @AllArgsConstructor
    public enum ContentType implements ConvertableEnum<Integer> {


        BARE(3001, "纯文本"),
        REMOTE_URL(3002, "远程URL"),
        LOCAL_URL(3003, "本地URL"),
        UNKNOWN(3004, "未知公告内容");


        @JsonValue
        private final Integer code;
        private final String comment;


    }*/


}