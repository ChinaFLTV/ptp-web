package ptp.fltv.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pfp.fltv.common.model.po.manage.User;
import pfp.fltv.common.model.vo.UserLoginVo;
import pfp.fltv.common.response.Result;
import ptp.fltv.web.mapper.UserMapper;
import ptp.fltv.web.service.UserService;
import ptp.fltv.web.utils.JwtUtils;

import java.util.Objects;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 7:12:27
 * @description 用户服务接口的实现类
 * @filename UserServiceImpl.java
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public User getUserByNickname(@NonNull String nickname) {

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickname, nickname);

        return baseMapper.selectOne(queryWrapper);

    }


    @Override
    public Result<String> login(UserLoginVo userLoginVo) {

        if (userLoginVo == null) {

            return Result.failure("登录数据受损！");

        }

        User user = getUserByNickname(userLoginVo.getNickname());

        if (user == null) {

            return Result.failure("用户名不存在！");

        } else if (!Objects.equals(passwordEncoder.encode(userLoginVo.getPassword()), user.getPassword())) {

            return Result.failure("用户密码错误！");

        }

        // TODO 保存用户敏感信息到分布式Redis中，同时返回对应的SessionId


        userLoginVo.setPassword("");// 2024-4-3  20:51-清除用户敏感信息
        String jwt = JwtUtils.encode(userLoginVo);

        // 2024-4-3  21:33-返回给前端，保存到LocalStorage那里，用的时候再让前端带过来
        return Result.success(jwt);

    }


}