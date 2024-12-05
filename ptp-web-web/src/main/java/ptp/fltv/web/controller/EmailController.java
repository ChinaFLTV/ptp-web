package ptp.fltv.web.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pfp.fltv.common.annotation.LogRecord;
import pfp.fltv.common.model.po.response.Result;
import ptp.fltv.web.service.EmailService;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/12/4 PM 10:45:08
 * @description 邮箱控制器
 * @filename EmailController.java
 */

@RequiredArgsConstructor
@Tag(name = "邮箱操作接口")
@RestController
@RequestMapping("/email")
public class EmailController {


    private final EmailService emailService;


    @LogRecord(description = "发送一条随机一次性验证码邮件到指定邮箱")
    @SentinelResource("web-content-dialogue-controller")
    @Operation(description = "发送一条随机一次性验证码邮件到指定邮箱")
    @GetMapping("/sendSingleEmailWithVerificationCode")
    public Result<String> sendSingleEmailWithVerificationCode(

            @Parameter(name = "email", description = "待发送到的邮箱地址", required = true) @RequestParam("email") String email

    ) {

        String verificationCode = emailService.sendSingleEmailWithVerificationCode(email);

        return StringUtils.hasLength(verificationCode) ? Result.success(verificationCode) : Result.failure(null);

    }


}
