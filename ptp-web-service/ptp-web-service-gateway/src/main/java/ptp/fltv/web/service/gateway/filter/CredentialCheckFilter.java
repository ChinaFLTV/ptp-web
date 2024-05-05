package ptp.fltv.web.service.gateway.filter;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import pfp.fltv.common.model.po.manage.Role;
import pfp.fltv.common.model.po.manage.User;
import pfp.fltv.common.utils.JwtUtils;
import ptp.fltv.web.service.gateway.constants.SecurityConstants;
import ptp.fltv.web.service.gateway.model.ApplicationContext;
import reactor.core.publisher.Mono;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/3 PM 9:06:45
 * @description 自定义的 Spring Cloud Gateway 进行登录校验的全局过滤器
 * @filename PtpGlobalFilter.java
 */

@Slf4j
@Component
public class CredentialCheckFilter implements GlobalFilter, Ordered {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        final ServerHttpRequest request = exchange.getRequest();
        final ServerHttpResponse response = exchange.getResponse();

        log.info("=========================================================================");
        log.info(String.format("IP为 %s 的用户访问了 %s 路径上运行的服务提供的API", request.getRemoteAddress(), request.getPath()));
        log.info("============请求头信息============");
        request.getHeaders().forEach((k, v) -> log.info(String.format("%s : %s", k, v.toString())));
        log.info("============请求Cookies信息============");
        log.info(request.getCookies().toString());

        // 2024-5-3  22:14-如果在permitAll分类中的路径，则直接放行
        final String path = request.getPath().value();
        if (confirmPermitAllPath(path)) {

            log.info("----> 访问路径没有安全防护，请求将被直接放行");
            // 2024-5-5  21:05-不管之前有没有缓存信息，在这里拦截返回了都必须清理上下文信息，以便对后续请求造成干扰
            ApplicationContext.clear();
            return chain.filter(exchange);

        }


        final String authorization = request.getHeaders().getFirst("Authorization");// 2024-5-3  21:17-取到用户登录后本次存储的JWT数据
        final String STORE_KEY = JwtUtils.decode(authorization);

        if (STORE_KEY != null) {

            final User compactUser = JSON.parseObject(stringRedisTemplate.opsForValue().get("user:login:" + STORE_KEY), User.class);
            final Role compactRole = JSON.parseObject(stringRedisTemplate.opsForValue().get("user:role:" + STORE_KEY), Role.class);

            // 2024-4-7  9:42-此时意味着用户SESSION KEY正常且未过期
            if (compactUser != null && compactRole != null) {

                // UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(compactUser, compactUser.getPassword(), compactRole.getGrantedAuthorities());
                // SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                log.info("----> 校验通过，请求将被允许放行");
                log.info("============请求用户信息[通过验证]============");
                log.info(compactUser.toString());
                log.info("============请求角色信息[通过验证]============");
                log.info(compactRole.toString());

                // 2024-5-5  21:00-缓存用户身份信息到ThreadLocal中，以便后续组件使用
                ApplicationContext.USER.set(compactUser);
                ApplicationContext.ROLE.set(compactRole);

                return chain.filter(exchange);

            } else {

                log.warn("----> 用户凭证过期或无效");
                log.warn("============请求用户信息[未通过验证]============");
                log.warn(compactUser != null ? compactUser.toString() : null);
                log.warn("============请求角色信息[未通过验证]============");
                log.warn(compactRole != null ? compactRole.toString() : null);

            }

        } else {

            log.warn("----> 用户凭证数据受损");

        }

        log.warn("----> 校验未通过，请求将被拒绝");
        // 2024-5-5  21:04-不管之前有没有缓存信息，在这里拦截返回了都必须清理上下文信息，以便对后续请求造成干扰
        ApplicationContext.clear();
        // 2024-5-3  21:50-用户凭证缺失、过期、其他原因失效，直接禁止访问
        // 2024-5-3  21:32-直接在SpringCloudGateway这边拦截
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();

    }


    @Override
    public int getOrder() {

        // 2024-5-4  21:07-赋予其最高优先级(请求发送进来第一个处理，响应送出去最后一个处理)
        return Integer.MIN_VALUE;

    }


    /**
     * @param path 当前访问的路径
     * @return 是否是PermitAll类型
     * @author Lenovo/LiGuanda
     * @date 2024/5/3 PM 10:16:23
     * @version 1.0.0
     * @description 确认当前访问路径是否是PermitAll类型
     * @filename CredentialCheckFilter.java
     */
    private boolean confirmPermitAllPath(String path) {

        if (!StringUtils.hasLength(path)) {

            return false;

        }

        for (String permitAllPath : SecurityConstants.PERMIT_ALL_PATHS) {

            if (path.startsWith(permitAllPath)) {

                return true;

            }

        }

        return false;

    }


}
