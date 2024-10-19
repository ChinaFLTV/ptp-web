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


}