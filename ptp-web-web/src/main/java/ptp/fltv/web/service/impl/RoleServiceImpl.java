package ptp.fltv.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import pfp.fltv.common.model.po.manage.Role;
import pfp.fltv.common.model.po.manage.User;
import ptp.fltv.web.mapper.RoleMapper;
import ptp.fltv.web.service.RoleService;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 7:06:59
 * @description 角色服务接口的实现类
 * @filename RoleServiceImpl.java
 */

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {


    @Override
    public Role getRoleByUser(User user) {

        if (user == null) {

            return Role.EMPTY_ROLE();

        }

        Role role = baseMapper.selectById(user.getId());

        return (role == null) ? Role.EMPTY_ROLE() : role;

    }


}