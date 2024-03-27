package ptp.fltv.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import pfp.fltv.common.model.po.manage.User;
import ptp.fltv.web.mapper.UserMapper;
import ptp.fltv.web.service.UserService;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 7:12:27
 * @description 用户服务接口的实现类
 * @filename UserServiceImpl.java
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


}