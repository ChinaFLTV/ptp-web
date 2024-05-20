package pfp.fltv.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/14 下午 9:57:11
 * @description 指明用户角色
 * @filename Role.java
 */

@Getter
@AllArgsConstructor
public enum Role {


    USER(11, "正常用户"),
    VIP_USER(12, "VIP用户"),
    SVIP_USER(13, "SVIP用户"),
    ABNORMAL_USER(15, "状态异常用户"),
    BLOCKED_USER(16, "封禁用户"),
    VISITOR(17, "游客"),
    ADMINISTRATOR(18, "管理员"),
    SUPER_ADMINISTRATOR(19, "超级管理员");


    @JsonValue
    private final Integer code;
    private final String comment;


}
