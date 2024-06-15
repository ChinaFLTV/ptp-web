package ptp.fltv.web.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pfp.fltv.common.model.po.content.Passage;
import pfp.fltv.common.response.Result;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/10 PM 9:02:39
 * @description 专门用于进行测试的API接口(生产环境禁用)
 * @filename TestController.java
 */

@Slf4j(topic = "ptp-web-web-test")
@Profile("dev")
@Tag(name = "WEB测试专用接口")
@RestController
@RequestMapping("/test")
public class TestController {


    @SentinelResource("web-content-test")
    @Operation(description = "获取达哥的女朋友")
    @GetMapping("/get/dage/wife")
    public Result<String> getDageWife(HttpServletRequest request) throws InterruptedException {

        String ip = request.getHeader("X-Forwarded-For");

        Thread.sleep(300);// 2024-5-10  21:13-模拟耗时操作
        log.info("IP为 {} 的用户发来一个获取达哥女朋友的GET请求~", ip);
        return Result.success("JuJingyi");

    }


    @SentinelResource("web-content-test")
    @Operation(description = "模拟超时获取达哥的女朋友")
    @GetMapping("/get/dage/wife/timeout")
    public Result<String> getDageWifeWithTimeoutOccurring() throws InterruptedException {

        Thread.sleep(5000);
        return Result.success("IU");

    }


    @SuppressWarnings("DataFlowIssue")
    @SentinelResource("web-content-test")
    @Operation(description = "模拟获取达哥的女朋友出现内部异常")
    @GetMapping("/get/dage/wife/exception")
    public Result<String> getDageWifeWithExceptionOccurring() throws InterruptedException {

        Thread.sleep(300);
        Passage passage = null;
        passage.setTitle("Breaking News");
        return Result.success("IU");

    }


}
