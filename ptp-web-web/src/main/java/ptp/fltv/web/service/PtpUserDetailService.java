package ptp.fltv.web.service;

import jakarta.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pfp.fltv.common.model.po.manage.Role;
import pfp.fltv.common.model.po.manage.User;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/31 下午 7:40:45
 * @description <code>{@UserDetailService}</code> 的自定义实现类
 * @filename PtpUserDetailService.java
 */

public class PtpUserDetailService implements UserDetailsService {


    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;


    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {

        User user = userService.getUserByNickname(nickname);
        if (user != null) {

            Role role = roleService.getRoleByUser(user);
            Set<GrantedAuthority> authorities = role.getAuthorities()
                    .stream()
                    .map((Function<String, GrantedAuthority>) SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());

            return new org.springframework.security.core.userdetails.User(user.getNickname(), user.getPassword(),
                    true, true, true, true, authorities);

        }
        return null;

    }


}
