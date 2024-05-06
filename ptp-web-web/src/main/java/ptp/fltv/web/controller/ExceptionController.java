package ptp.fltv.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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


    @Operation(description = "流量过载")
    @GetMapping("/flow/overload")
    public Result<?> flowOverload(

            @Parameter(name = "message") @RequestParam(name = "message", required = false) String message

    ) {

        return Result.failure(StringUtils.hasLength(message) ? message : "The flow exceeds the limit , please try again later!");

    }


    @Operation(description = "服务异常")
    @GetMapping("/service/abnormal")
    public Result<?> serviceAbnormal(

            @Parameter(name = "message") @RequestParam(name = "message", required = false) String message

    ) {

        return Result.failure(StringUtils.hasLength(message) ? message : "The current service is abnormal , please try again later!");

    }


    @Operation(description = "权限校验失败")
    @GetMapping("/authentication/fail")
    public Result<?> authenticationFail(

            @Parameter(name = "message") @RequestParam(name = "message", required = false) String message

    ) {

        return Result.failure(StringUtils.hasLength(message) ? message : "You are not authorized to access this page !");

    }


}