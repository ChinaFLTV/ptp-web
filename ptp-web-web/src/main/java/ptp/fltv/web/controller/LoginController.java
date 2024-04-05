package ptp.fltv.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pfp.fltv.common.model.vo.UserLoginVo;
import pfp.fltv.common.response.Result;
import ptp.fltv.web.service.UserService;

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


    @PostMapping("/login")
    public Result<?> login(@RequestBody UserLoginVo userLoginVo) {

        return userService.login(userLoginVo);

    }


    @GetMapping("/logout")
    public Result<String> logout() {

        // 2024-4-5  21:21-这边登出的时候，前端那边也要同步清除用户SESSION TOKEN信息，不需要清除登录信息
        SecurityContextHolder.clearContext();
        return Result.success("期待与您的下一次相遇~");

    }


}
