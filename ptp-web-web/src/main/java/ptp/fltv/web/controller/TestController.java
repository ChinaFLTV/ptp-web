package ptp.fltv.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pfp.fltv.common.response.Result;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/10 PM 9:02:39
 * @description 专门用于进行测试的API接口(生产环境禁用)
 * @filename TestController.java
 */

@Profile("dev")
@Tag(name = "WEB测试专用接口")
@RestController
@RequestMapping("/test")
public class TestController {


    @Operation(description = "获取达哥的女朋友")
    @GetMapping("/get/dage/wife")
    public Result<String> getDageWife() throws InterruptedException {

        Thread.sleep(300);// 2024-5-10  21:13-模拟耗时操作
        return Result.success("JuJingyi");

    }


}
