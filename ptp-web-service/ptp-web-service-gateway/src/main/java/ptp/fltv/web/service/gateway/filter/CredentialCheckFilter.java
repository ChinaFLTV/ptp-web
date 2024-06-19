package ptp.fltv.web.service.gateway.filter;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import pfp.fltv.common.enums.LoginClientType;
import pfp.fltv.common.model.po.manage.Role;
import pfp.fltv.common.model.po.manage.User;
import pfp.fltv.common.response.Result;
import pfp.fltv.common.utils.JwtUtils;
import ptp.fltv.web.service.gateway.constants.SecurityConstants;
import ptp.fltv.web.service.gateway.model.ApplicationContext;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/3 PM 9:06:45
 * @description 自定义的 Spring Cloud Gateway 进行登录校验的全局过滤器
 * @filename PtpGlobalFilter.java
 */

@AllArgsConstructor
@Slf4j
@Component
public class CredentialCheckFilter implements GlobalFilter, Ordered {

    private StringRedisTemplate stringRedisTemplate;


    @SuppressWarnings("unchecked")
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 2024-5-12  22:41-由于该过滤器优先级最高，最接近外界，也就最先接触到上一次请求的遗留内容，因此首要任务就是请求可能存在的遗留的上下文信息
        // 2024-5-5  21:05-不管之前有没有缓存信息，在这里拦截返回了都必须清理上下文信息，以便对后续请求造成干扰
        ApplicationContext.clear();

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        log.info("=========================================================================");
        log.info(String.format("IP为 %s 的用户访问了 %s 路径上运行的服务提供的API", request.getRemoteAddress(), request.getPath()));
        log.info("============请求头信息============");
        request.getHeaders().forEach((k, v) -> log.info(String.format("%s : %s", k, v.toString())));
        log.info("============请求Cookies信息============");
        log.info(request.getCookies().toString());

        // 2024-5-3  22:14-如果在permitAll分类中的路径，则直接放行
        String path = request.getPath().value();
        if (confirmPermitAllPath(path)) {

            log.info("----> 访问路径没有安全防护，请求将被直接放行");
            // 2024-5-12  22:30-置无条件放行标志位为true
            ApplicationContext.IS_PERMITTED_DIRECTLY.set(true);
            return chain.filter(exchange);

        }

        // 2024-6-18  19:07-存放校验失败时的异常消息信息(To User)
        String failedMsg;

        String authorization = request.getHeaders().getFirst("Authorization");// 2024-5-3  21:17-取到用户登录后本次存储的JWT数据

        if (StringUtils.hasLength(authorization)) {

            String STORE_KEY = JwtUtils.decode(authorization);
            if (STORE_KEY != null) {

                final User compactUser = JSON.parseObject(stringRedisTemplate.opsForValue().get("user:login:" + STORE_KEY), User.class);
                final Role compactRole = JSON.parseObject(stringRedisTemplate.opsForValue().get("user:role:" + STORE_KEY), Role.class);

                // 2024-4-7  9:42-此时意味着用户SESSION KEY正常且未过期
                if (compactUser != null && compactRole != null) {

                    // UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(compactUser, compactUser.getPassword(), compactRole.getGrantedAuthorities());
                    // SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    log.info("============请求用户信息[通过验证]============");
                    log.info(compactUser.toString());
                    log.info("============请求角色信息[通过验证]============");
                    log.info(compactRole.toString());

                    HttpCookie loginClientInfo = request.getCookies().getFirst("login_client_info");
                    if (loginClientInfo != null) {


                        String encodedLoginClientInfo = loginClientInfo.getValue();
                        // 2024-6-17  23:04-之所以强制前端先加密登录环境信息再发送登录请求，是因为这样可在一定程度上避免客户端伪造登录信息的情况
                        String decodedLoginClientInfo = JwtUtils.decode(encodedLoginClientInfo);
                        HashMap<String, Object> nativeEnvMap = JSON.parseObject(decodedLoginClientInfo, HashMap.class);
                        LoginClientType nativeClientType = LoginClientType.valueOfByCode((Integer) Objects.requireNonNull(nativeEnvMap).get("client-type"));
                        String nativeDeviceId = (String) nativeEnvMap.getOrDefault("device-id", "NATIVE-UNKNOWN-DEVICEID");
                        String nativeLoginDatetime = (String) nativeEnvMap.getOrDefault("login-datetime", "NATIVE-UNKNOWN-DATETIME");

                        String loginEnvInfoStr = stringRedisTemplate.opsForValue().get(String.format("user:login:env:%s:%s", nativeClientType.name().toLowerCase(), STORE_KEY));

                        if (loginEnvInfoStr != null) {

                            // 2024-6-17  23:06-说明目前已有该ID的登录历史，先判断客户端类型和机器码是否一致，如果一致，则不做操作，否则将更新当前端的登录环境数据并发送下线通知给另个同时在线的同ID的用户
                            HashMap<String, Object> cloudEnvMap = JSON.parseObject(loginEnvInfoStr, HashMap.class);
                            LoginClientType cloudClientType = LoginClientType.valueOfByCode((Integer) cloudEnvMap.get("client-type"));
                            String cloudDeviceId = (String) cloudEnvMap.getOrDefault("device-id", "CLOUD-UNKNOWN-DEVICEID");
                            String cloudLoginDatetime = (String) cloudEnvMap.getOrDefault("login-datetime", "CLOUD-UNKNOWN-DATETIME");

                            // 2024-6-17  23:23-也就是说，此时已经存在同ID的同端用户处于登录状态了，需要强制其下线
                            if (!nativeDeviceId.equals(cloudDeviceId)) {

                                // stringRedisTemplate.opsForValue().set(String.format("user:login:env:%s:%s", nativeClientType.name().toLowerCase(), STORE_KEY), JSON.toJSONString(nativeEnvMap), RedisConstants.CACHE_TIMEOUT, TimeUnit.MILLISECONDS);
                                // 2024-6-18  10:59-非登录请求到达该点时，说明此时已经存在同端的同ID的用户异地登录了，当前登录信息失效了，因此需要拒绝该请求，
                                // 同时，客户端本地还需要强制清除用户的本地登录数据，准备进行重新登录
                                // 该过程还需要向客户端返回当前最新的登录环境数据，以警告用户存在另一台物理设备正在同端登录
                                log.info("============请求用户云端登录信息[未通过验证]============");
                                log.warn("cloud-client-type = {}", cloudClientType);
                                log.warn("cloud-device-id = {}", cloudDeviceId);

                                ApplicationContext.clear();

                                // 2024-6-18  20:45-再补充一点请求异常的信息
                                cloudEnvMap.put("message", "请求用户云端登录信息与本地登录信息不符(可能存在异地登录的危险情况)");
                                DataBuffer dataBuffer = response.bufferFactory()
                                        .wrap(JSON.toJSONString(Result.failure(cloudEnvMap)).getBytes(StandardCharsets.UTF_8));

                                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                                response.setStatusCode(HttpStatus.FORBIDDEN);
                                return response.writeWith(Mono.just(dataBuffer));

                            } else {

                                log.info("----> 校验通过，请求将被允许放行");
                                log.info("============请求用户云端登录信息[通过验证]============");
                                log.warn("cloud-client-type = {}", cloudClientType);
                                log.warn("cloud-device-id = {}", cloudDeviceId);

                                // 2024-5-5  21:00-缓存用户身份信息到ThreadLocal中，以便后续组件使用
                                ApplicationContext.USER.set(compactUser);
                                ApplicationContext.ROLE.set(compactRole);

                                return chain.filter(exchange);

                            }


                        } else {

                            log.warn("----> 用户云端登录环境信息丢失或出现异常");
                            log.warn("============请求用户本地登录环境[未通过验证]============");
                            log.warn("native-client-type = {}", nativeClientType);
                            log.warn("native-device-id = {}", nativeDeviceId);
                            failedMsg = "用户云端登录环境信息丢失或出现异常";

                        }


                    } else {

                        log.warn("----> 用户本地登录环境信息缺失或受损");
                        failedMsg = "用户本地登录环境信息缺失或受损";

                    }

                } else {

                    log.warn("----> 用户凭证过期或无效");
                    log.warn("============请求用户信息[未通过验证]============");
                    log.warn(compactUser != null ? compactUser.toString() : null);
                    log.warn("============请求角色信息[未通过验证]============");
                    log.warn(compactRole != null ? compactRole.toString() : null);
                    failedMsg = "用户凭证过期或无效";

                }

            } else {

                log.warn("----> 用户凭证数据受损");
                failedMsg = "用户凭证数据受损";

            }

        } else {

            log.warn("----> 用户请求未携带凭证信息");
            failedMsg = "用户请求未携带凭证信息";

        }

        log.warn("----> 校验未通过，请求将被拒绝");
        // 2024-5-5  21:04-不管之前有没有缓存信息，在这里拦截返回了都必须清理上下文信息，以便对后续请求造成干扰
        ApplicationContext.clear();

        // 2024-5-3  21:50-用户凭证缺失、过期、其他原因失效，直接禁止访问
        // 2024-5-3  21:32-直接在SpringCloudGateway这边拦截
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.FORBIDDEN);
        DataBuffer dataBuffer = response.bufferFactory().wrap(JSON.toJSONString(Result.failure(failedMsg)).getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(dataBuffer));

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
