package pfp.fltv.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pfp.fltv.common.enums.base.ConvertableEnum;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/25 PM 10:27:03
 * @description 交易订单的状态枚举类
 * @filename RecordStatus.java
 */

@Getter
@AllArgsConstructor
public enum RecordStatus implements ConvertableEnum<Integer> {


    NORMAL(1301, "正常"),
    PENDING(1302, "待定"), // 2024-5-25  22:30-此种情况指用户在支付订单界面产生犹豫而处于迟迟未支付状态
    COMPLETE(1303, "完成"),// 2024-5-25  22:32-含义待定(占位)
    CANCEL(1304, "取消"),
    ABORT(1305, "中止"), // 2024-5-25  22:31-因意外情况中止，可能为用户状态出现异常等情况
    COMMIT(1306, "已提交"), // 2024-5-25  22:33-用户一方已提交订单，系统还在处理的状态
    ROLLBACK(1307, "回退"); // 2024-5-25  22:33-与cancel不同的是，cancel是用户一方主动取消订单，而rollback则侧重服务方出现异常而强制取消了订单


    @JsonValue
    private final Integer code;
    private final String comment;


}