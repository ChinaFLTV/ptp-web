package ptp.fltv.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pfp.fltv.common.exceptions.PtpException;
import pfp.fltv.common.model.vo.UserLoginVo;
import pfp.fltv.common.response.Result;
import ptp.fltv.web.service.UserService;
import pfp.fltv.common.utils.JwtUtils;

import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/16 下午 8:28:57
 * @description 用于控制用户登录注册的控制器
 * @filename LoginController.java
 */

@Tag(name = "登录接口")
@RestController
@RequestMapping("/gate")
public class LoginController {


    @Autowired
    private UserService userService;


    @Operation(description = "普通登录(用户名+密码)")
    @PermitAll
    @PostMapping("/login")
    public Result<?> login(@RequestBody UserLoginVo userLoginVo) throws PtpException {

        // 2024-4-7  22:26-登录前，客户端一侧一定要将本地的登录数据清理干净

        Map<String, Object> userData = userService.login(userLoginVo);

        return Result.success(userData);

    }


    @Operation(description = "登出账号")
    @GetMapping("/logout")
    public Result<String> logout() {

        // 2024-4-5  21:21-这边登出的时候，前端那边也要同步清除用户SESSION TOKEN信息，不需要清除登录信息
        // SecurityContextHolder.clearContext();
        return Result.success("期待与您的下一次相遇~");

    }


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
