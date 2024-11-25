package ptp.fltv.web.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pfp.fltv.common.annotation.LogRecord;
import pfp.fltv.common.constants.WebConstants;
import pfp.fltv.common.enums.LoginClientType;
import pfp.fltv.common.exceptions.PtpException;
import pfp.fltv.common.model.po.response.Result;
import pfp.fltv.common.model.vo.UserLoginVo;
import pfp.fltv.common.utils.JwtUtils;
import ptp.fltv.web.service.UserService;

import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/16 下午 8:28:57
 * @description 用于控制用户登录注册的控制器
 * @filename LoginController.java
 */

@AllArgsConstructor
@Tag(name = "登录接口")
@RestController
@RequestMapping("/gate")
public class LoginController {


    private UserService userService;


    @LogRecord(description = "普通登录(用户名+密码)")
    @SentinelResource("web-gate-controller")
    @Operation(description = "普通登录(用户名+密码)")
    @PermitAll
    @PostMapping("/login")
    public Result<Map<String, Object>> login(

            @RequestBody UserLoginVo userLoginVo,
            HttpServletResponse response

    ) throws PtpException {

        // 2024-4-7  22:26-登录前，客户端一侧一定要将本地的登录数据清理干净

        Map<String, Object> userData = userService.loginByNicknameAndPassword(userLoginVo);

        if (userData != null) {

            response.addCookie(generateCookie(userData));
            return Result.success(userData);

        } else {

            return Result.failure(null);

        }

    }


    @LogRecord(description = "QQ登录(OpenID)")
    @SentinelResource("web-gate-controller")
    @Operation(description = "QQ登录(OpenID)")
    @PermitAll
    @PostMapping("/loginByQQ")
    public Result<Map<String, Object>> loginByQQ(

            @RequestBody UserLoginVo userLoginVo,
            HttpServletResponse response

    ) throws PtpException {

        Map<String, Object> userData = userService.loginByQQ(userLoginVo);

        if (userData != null) {

            response.addCookie(generateCookie(userData));
            return Result.success(userData);

        } else {

            return Result.failure(null);

        }

    }


    @LogRecord(description = "普通注册(用户名+密码)")
    @SentinelResource("web-gate-controller")
    @Operation(description = "普通注册(用户名+密码)")
    @PermitAll
    @PostMapping("/register")
    public Result<Map<String, Object>> register(

            @RequestBody UserLoginVo userLoginVo,
            HttpServletResponse response

    ) throws PtpException {


        Map<String, Object> userData = userService.registerByNicknameAndPassword(userLoginVo);

        if (userData != null) {

            response.addCookie(generateCookie(userData));
            return Result.success(userData);

        } else {

            return Result.failure(null);

        }

    }


    /**
     * @param userData 配置Cookie所需的用户登录数据包
     * @return 需要回填给客户端/前端的Cookie
     * @author Lenovo/LiGuanda
     * @date 2024/11/24 PM 8:02:38
     * @version 1.0.0
     * @description 配置登录成功后回填的Cookie数据
     * @filename LoginController.java
     */
    private Cookie generateCookie(Map<String, Object> userData) {

        // 2024-8-4  17:29-主动设置用户登录环境Cookie的数据到客户端浏览器中
        Cookie cookie = new Cookie(WebConstants.USER_LOGIN_COOKIE_KEY, (String) userData.getOrDefault(WebConstants.USER_LOGIN_COOKIE_KEY, "UNKNOWN"));
        cookie.setSecure(false); // 2024-8-4  17:26-带有该Secure属性的 cookie 仅通过 HTTPS 协议与加密请求一起发送到服务器 , 将此属性设置为 true 以防止中间人附加
        cookie.setHttpOnly(false); // 2024-8-4  17:27-JavaScript Document.cookie API 无法访问具有该属性的 cookie , 将此属性设置为 true 以防止跨站点脚本 (XSS) 攻击
        cookie.setMaxAge(7 * 24 * 60 * 60); // 2024-8-4  17:28-设置 cookie 的过期时间 , 默认为-1. -1表示它是一个会话 Cookie , 当用户关闭浏览器时 , 会话 cookie 将被删除
        cookie.setDomain(ptp.fltv.web.constants.WebConstants.SERVER_IP);
        cookie.setPath("/");

        return cookie;

    }


    @LogRecord(description = "登出账号")
    @SentinelResource("web-gate-controller")
    @Operation(description = "登出账号")
    @PostMapping("/logout")
    public Result<String> logout(@RequestParam("clientType") LoginClientType clientType, @RequestParam("userId") Long userId, HttpServletResponse response) {

        userService.logout(clientType, userId, response);
        return Result.success("期待与您的下一次相遇~");

    }


    @LogRecord(description = "通过Github第三方客户端进行OAuth2登录")
    @SentinelResource("web-gate-controller")
    @Operation(summary = "通过Github第三方客户端进行OAuth2登录", description = "想要被动地调用此方法，需要向 https://github.com/login/oauth/authorize?client_id=你的应用注册的ClientID 发送GET请求")
    @PermitAll
    @GetMapping("/login/oauth2/github")
    public Result<?> loginByGithub(@RequestParam("code") String code) {

        // 2024-4-7  22:25-登录前，客户端一侧一定要将本地的登录数据清理干净

        String userInfo = userService.loginByGithub(code);

        // 2024-4-7  22:05-加密后返回给前端处理，最好将此凭证保存起来，以便日后复用
        String encodedUserInfo = JwtUtils.encode(userInfo);
        return Result.success(encodedUserInfo);

    }


}
