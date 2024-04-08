package ptp.fltv.web.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pfp.fltv.common.constants.OAuth2LoginConstants;
import pfp.fltv.common.constants.RedisConstants;
import pfp.fltv.common.exceptions.PtpException;
import pfp.fltv.common.model.po.manage.Role;
import pfp.fltv.common.model.po.manage.User;
import pfp.fltv.common.model.vo.UserLoginVo;
import ptp.fltv.web.mapper.UserMapper;
import ptp.fltv.web.service.RoleService;
import ptp.fltv.web.service.UserService;
import ptp.fltv.web.utils.JwtUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RoleService roleService;


    @Override
    public User getUserByNickname(@NonNull String nickname) {

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickname, nickname);

        return baseMapper.selectOne(queryWrapper);

    }


    @Override
    public Map<String, Object> login(UserLoginVo userLoginVo) throws PtpException {

        if (userLoginVo == null) {

            throw new PtpException(801);

        }

        User user = getUserByNickname(userLoginVo.getNickname());

        if (user == null) {

            throw new PtpException(802);

        } else if (!Objects.equals(passwordEncoder.encode(userLoginVo.getPassword()), user.getPassword())) {

            throw new PtpException(803);

        }

        // 2024-4-4  21:18 保存用户敏感信息和权限认证信息到分布式Redis中，同时返回对应的SessionId

        Role role = roleService.getRoleByUser(user);

        Role compactRole = Role.builder()
                .id(role.getId())
                .name(role.getName())
                .authorities(role.getAuthorities())
                .prohibition(role.getProhibition())
                .build();

        // 2024-4-4  21:10-为避免user数据过大，将适当地对一些非必要字段进行赋空值操作
        User compactUser = User.builder()
                .id(user.getId())
                .account(user.getAccount())
                .password(user.getPassword())
                .status(user.getStatus())
                .roleId(user.getId())
                .build();

        // 2024-4-5  22:17-先不要用ID+""的形式加密，这样做解密的时候会被转换为"ID"
        // TODO 目前先用用户ID作为key，日后将单独与远程随机ID生成服务进行交互获取
        final Long STORE_KEY = user.getId();

        // 2024-4-7  9:39-缓存的超时时间暂定为24H
        redisTemplate.opsForValue().set("user:login:" + STORE_KEY, JSON.toJSONString(compactUser), RedisConstants.CACHE_TIMEOUT, TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set("user:role:" + STORE_KEY, JSON.toJSONString(compactRole), RedisConstants.CACHE_TIMEOUT, TimeUnit.MILLISECONDS);

        Map<String, Object> result = new HashMap<>();
        userLoginVo.setPassword(""); // 2024-4-3  20:51-清除用户敏感信息
        String jwt1 = JwtUtils.encode(userLoginVo);
        result.put("userLoginData", jwt1);
        String jwt2 = JwtUtils.encode(STORE_KEY);
        result.put("STORE_KEY", jwt2);

        // 2024-4-3  21:33-返回给前端，保存到LocalStorage那里，用的时候再让前端带过来
        return result;

    }


    @Override
    public String loginByGithub(String code) {

        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", OAuth2LoginConstants.GITHUB_CLIENT_ID);
        params.put("client_secret", OAuth2LoginConstants.GITHUB_CLIENT_SECRET);

        String credential = HttpUtil.createPost(OAuth2LoginConstants.GITHUB_ACCESS_TOKEN_URL)
                .header("Accept", "application/json")
                .form(params)
                .execute()
                .body();

        String token = JSON.parseObject(credential).getString("access_token");

        return HttpUtil.createGet(OAuth2LoginConstants.GITHUB_ACCESS_USER_INFO_URL)
                .header("Accept", "application/json")
                .header("Authorization", "token " + token)
                .execute()
                .body();

    }


}