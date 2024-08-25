package ptp.fltv.web.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pfp.fltv.common.annotation.LogRecord;
import pfp.fltv.common.model.po.response.Result;
import ptp.fltv.web.service.SystemService;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/8/25 PM 9:25:59
 * @description 用于获取服务端的相关状态信息的控制器
 * @filename SystemController.java
 */

// 2024-8-25  22:53-该API接口默认只要用户登录了 , 就可以无需额外的权限即可访问(即它将不匹配现有配置的任何一条权限规则)
@AllArgsConstructor
@Tag(name = "系统状态操作接口")
@RestController
@RequestMapping("/system/status")
public class SystemController {


    private SystemService systemService;


    @LogRecord(description = "查询服务器的IP地址(一般用于开发环境)")
    @SentinelResource("web-system-status-controller")
    @Operation(description = "查询服务器的IP地址(一般用于开发环境)")
    @GetMapping("/query/server/ip")
    public Result<String> queryServerIp() {

        return Result.success(systemService.getServerIp());

    }


    @LogRecord(description = "查询客户端的IP地址")
    @SentinelResource("web-system-status-controller")
    @Operation(description = "查询客户端的IP地址")
    @GetMapping("/query/client/ip")
    public Result<String> queryClientIp(HttpServletRequest request) {

        String ipAddress = request.getHeader("X-Forwarded-For");

        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {

            ipAddress = request.getHeader("Proxy-Client-IP");

        }

        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {

            ipAddress = request.getHeader("WL-Proxy-Client-IP");

        }

        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {

            ipAddress = request.getRemoteAddr();

        }

        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {

            ipAddress = request.getRemoteAddr();

        }

        return Result.success(ipAddress);

    }


}
