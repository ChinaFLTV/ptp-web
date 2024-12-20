package ptp.fltv.web.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pfp.fltv.common.annotation.LogRecord;
import pfp.fltv.common.model.po.content.Passage;
import pfp.fltv.common.model.po.response.Result;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/10 PM 9:02:39
 * @description 专门用于进行测试的API接口(生产环境禁用)
 * @filename TestController.java
 */

@AllArgsConstructor
@Slf4j
@Profile("dev")
@Tag(name = "WEB测试专用接口")
@RestController
@RequestMapping("/test")
public class TestController {


    @LogRecord(description = "获取达哥的女朋友!!!")
    @SentinelResource("web-content-test")
    @Operation(description = "获取达哥的女朋友")
    @GetMapping("/get/dage/wife")
    public Result<String> getDageWife() throws InterruptedException {


        Thread.sleep(300);// 2024-5-10  21:13-模拟耗时操作
        return Result.success("JuJingyi");

    }


    @LogRecord(description = "模拟超时获取达哥的女朋友")
    @SentinelResource("web-content-test")
    @Operation(description = "模拟超时获取达哥的女朋友")
    @GetMapping("/get/dage/wife/timeout")
    public Result<String> getDageWifeWithTimeoutOccurring() throws InterruptedException {

        Thread.sleep(5000);
        return Result.success("IU");

    }


    @SuppressWarnings("DataFlowIssue")
    @LogRecord(description = "模拟获取达哥的女朋友出现内部异常")
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
