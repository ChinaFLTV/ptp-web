package ptp.fltv.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import pfp.fltv.common.model.po.manage.Permission;
import ptp.fltv.web.mapper.PermissionMapper;
import ptp.fltv.web.service.PermissionService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/12/6 PM 3:58:13
 * @description 角色权限服务接口的实现类
 * @filename PermissionServiceImpl.java
 */

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {


    @Override
    public List<Permission> findPermissionsByRoleId(@Nonnull Long roleId) {

        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);

        List<Permission> permissions = list(queryWrapper);

        return permissions == null ? new ArrayList<>() : permissions;

    }


}