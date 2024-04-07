package ptp.fltv.web.security.filter;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pfp.fltv.common.model.po.manage.Role;
import pfp.fltv.common.model.po.manage.User;
import ptp.fltv.web.utils.JwtUtils;

import java.io.IOException;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/4 下午 9:24:50
 * @description 对用户发起的请求进行权限校验的过滤器
 * @filename AuthorityCheckFilter.java
 */

@Component
public class AuthorityCheckFilter extends OncePerRequestFilter {


    @Autowired
    private StringRedisTemplate redisTemplate;


    /**
     * @param request     客户端的请求
     * @param response    服务端的响应
     * @param filterChain 过滤器链
     * @author Lenovo/LiGuanda
     * @date 2024/4/4 下午 9:43:28
     * @version 1.0.0
     * @description 根据客户端存储的SESSION KEY的TOKEN，向分布式Redis搜寻对应的User和Role信息，然后封装到Authentication中
     * @filename AuthorityCheckFilter.java
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull FilterChain filterChain) throws ServletException, IOException {

        String STORE_KEY = JwtUtils.decode(request.getHeader("Authorization"));

        if (STORE_KEY != null) {

            User compactUser = JSON.parseObject(redisTemplate.opsForValue().get("user:login:" + STORE_KEY), User.class);
            Role compactRole = JSON.parseObject(redisTemplate.opsForValue().get("user:role:" + STORE_KEY), Role.class);

            // 2024-4-7  9:42-此时意味着用户SESSION KEY正常且未过期
            if (compactUser != null && compactRole != null) {

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(compactUser, compactUser.getPassword(), compactRole.getGrantedAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }

            // 2024-4-7  9:43-用户凭证过期，啥也不做，后续访问受限方法时将引导至登录界面

        }

        // 2024-4-4  21:35-无论认证是否通过，都要放行该请求到其他过滤器(大不了认证权限为空~)
        filterChain.doFilter(request, response);

    }


}
