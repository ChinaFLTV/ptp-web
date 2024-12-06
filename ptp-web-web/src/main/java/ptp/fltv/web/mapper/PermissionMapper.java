package ptp.fltv.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import pfp.fltv.common.model.po.manage.Permission;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/12/6 PM 3:59:03
 * @description 角色权限Mapper
 * @filename PermissionMapper.java
 */

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {


}