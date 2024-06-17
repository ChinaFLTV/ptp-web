package ptp.fltv.web.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pfp.fltv.common.annotation.LogRecord;
import pfp.fltv.common.response.Result;
import ptp.fltv.web.service.PassageService;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/1 PM 7:35:47
 * @description 流量或服务异常相关的控制器
 * @filename ExceptionController.java
 */

@Tag(name = "流量或服务异常相关的接口")
@RestController
@RequestMapping("/exception")
public class ExceptionController {

    @Autowired
    private PassageService passageService;


    @LogRecord(description = "流量过载")
    @SentinelResource("web-exception-controller")
    @Operation(description = "流量过载")
    @GetMapping("/flow/overload")
    public Result<?> flowOverload(

            @Parameter(name = "message") @RequestParam(name = "message", required = false) String message

    ) {

        return Result.failure(StringUtils.hasLength(message) ? message : "The flow exceeds the limit , please try again later!");

    }


    @LogRecord(description = "服务异常")
    @SentinelResource("web-exception-controller")
    @Operation(description = "服务异常")
    @GetMapping("/service/abnormal")
    public Result<?> serviceAbnormal(

            @Parameter(name = "message") @RequestParam(name = "message", required = false) String message

    ) {

        return Result.failure(StringUtils.hasLength(message) ? message : "The current service is abnormal , please try again later!");

    }


    @LogRecord(description = "服务暂不可用")
    @SentinelResource("web-exception-controller")
    @Operation(description = "服务暂不可用")
    @GetMapping("/service/unavailable")
    public Result<?> serviceUnavailable(

            @Parameter(name = "message") @RequestParam(name = "message", required = false) String message

    ) {

        return Result.failure(StringUtils.hasLength(message) ? message : "The current service is unavailable , please try again later!");

    }


    @LogRecord(description = "权限校验失败")
    @SentinelResource("web-exception-controller")
    @Operation(description = "权限校验失败")
    @GetMapping("/authentication/fail")
    public Result<?> authenticationFail(

            @Parameter(name = "message") @RequestParam(name = "message", required = false) String message

    ) {

        return Result.failure(StringUtils.hasLength(message) ? message : "You are not authorized to access this page !");

    }


}