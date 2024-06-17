package ptp.fltv.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.annotation.Nonnull;
import pfp.fltv.common.model.po.manage.Role;
import pfp.fltv.common.model.po.manage.User;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 7:06:23
 * @description 角色服务接口
 * @filename RoleService.java
 */

public interface RoleService extends IService<Role> {


    /**
     * @param user 待查询角色信息的用户
     * @return 查询到的用户对应的角色数据，若用户为null则返回为空链表
     * @author Lenovo/LiGuanda
     * @date 2024/3/31 下午 8:26:19
     * @version 1.0.0
     * @description 根据传入的用户对象获取对应的角色数据
     * @filename RoleService.java
     */
    Role getRoleByUser(@Nonnull User user);


}
