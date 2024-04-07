package ptp.fltv.web.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/6 下午 9:10:40
 * @description 自定义的方法级别的权限校验器
 * @filename PermissionChecker.java
 */

@Component("pc")
public class PermissionChecker {


    /**
     * @param permissions 方法所要求的权限
     * @return 用户是否已具备必要的权限
     * @author Lenovo/LiGuanda
     * @date 2024/4/6 下午 9:18:12
     * @version 1.0.0
     * @description 判断用户是否具有方法上注明的权限中的任意一个或多个
     * @filename PermissionChecker.java
     */
    public boolean hasAnyPermission(String... permissions) {

        // 2024-4-6  21:24-此方法是开放型方法，直接放行
        if (permissions.length == 0) {

            return true;

        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2024-4-7  22:02-其实这里Authentication几乎是不为空的，因为如果为空的话，早在过滤器链那边就给拦截下来了，当然，也有可能用户直接访问不是SS保护的路径，那这种情况下就需要判断是否为空了
        // 2024-4-6  21:23-用户未登录的情况或者登录过期
        if (authentication == null) {

            // TODO 这里需要跳转到登录页面(通过抛出未登录异常的形式)
            return false;

        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        Set<String> requiredPermissions = Arrays.stream(permissions)
                .collect(Collectors.toSet());
        Set<String> grantedPermissions = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        // 2024-4-6  21:31-求取所需权限集合和已持有权限集合的交集，交集意味着用户所持有权限在所需权限中的候选权限的集合，若不为空，则证明用户可以访问该方法
        grantedPermissions.retainAll(requiredPermissions);

        return !grantedPermissions.isEmpty();

    }


}
