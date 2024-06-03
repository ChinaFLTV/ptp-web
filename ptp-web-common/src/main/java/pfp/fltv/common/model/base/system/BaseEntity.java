package pfp.fltv.common.model.base.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Transient;
import pfp.fltv.common.enums.TaskStatus;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/18 下午 10:14:06
 * @description 消息流实体类的基类
 * @filename BaseEntity.java
 */

@Schema(description = "消息流实体类的基类")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@SuperBuilder
public class BaseEntity implements Serializable {


    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "流水ID")
    private Long id;

    @Schema(description = "动作发起者ID")
    private Long uid;

    @Schema(description = "流水状态")
    private TaskStatus status = TaskStatus.NORMAL;

    @Schema(description = "其他数据配置(JSON)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String meta;

    @Schema(description = "动作产生时间")
    private Timestamp createTime;

    @Schema(description = "(最后)修改时间")
    private Timestamp updateTime;

    @TableLogic
    @Schema(description = "事件是否已被逻辑移除")
    private Integer is_deleted = 0;

    @Transient
    @Schema(description = "当前实体的版本(用于辅助实现乐观锁)")
    @Version
    private Integer version = 1;


}
