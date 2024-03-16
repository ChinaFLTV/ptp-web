package ptp.fltv.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/16 下午 8:28:57
 * @description 用于控制用户登录注册的控制器
 * @filename LoginController.java
 */

@RestController
@RequestMapping("/into")
public class LoginController {


    @GetMapping("/login")
    public String login() {

        return "登录成功！";

    }


}
