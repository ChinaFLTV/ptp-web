package ptp.fltv.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.annotation.Nonnull;
import pfp.fltv.common.model.po.manage.Permission;

import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/12/6 PM 3:57:34
 * @description 角色权限服务接口
 * @filename PermissionService.java
 */

public interface PermissionService extends IService<Permission> {


    /**
     * @param roleId 角色ID
     * @return 附属于该角色ID下的全部权限数据列表 , 如果角色ID不存在则返回空列表
     * @author Lenovo/LiGuanda
     * @date 2024/12/6 PM 4:06:00
     * @version 1.0.0
     * @description 根据角色ID获取附属于其之下的全部权限列表
     * @filename PermissionService.java
     */
    List<Permission> findPermissionsByRoleId(@Nonnull Long roleId);


}