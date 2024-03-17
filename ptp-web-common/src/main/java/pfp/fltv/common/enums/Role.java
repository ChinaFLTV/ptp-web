package pfp.fltv.common.enums;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/14 下午 9:57:11
 * @description 指明用户角色
 * @filename Role.java
 */

public enum Role {

    USER("正常用户", 1),
    VIP_USER("VIP用户", 2),
    SVIP_USER("SVIP用户", 3),
    ADMINISTRATOR("正常用户", 4),
    ABNORMAL_USER("状态异常用户", 5),
    FORBIDDEN_USER("封禁用户", 6),
    VISITOR("游客", 7);


    private final String comment;
    private final Integer roleCode;


    Role(String comment, Integer roleCode) {

        this.comment = comment;
        this.roleCode = roleCode;

    }


    public String getComment() {

        return comment;

    }


    public Integer getRoleCode() {

        return roleCode;

    }


}
