package ptp.fltv.web.service.gateway.handler;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import pfp.fltv.common.model.po.response.Result;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/13 PM 9:48:39
 * @description 服务熔断事件的处理器
 * @filename CircuitBreakerHandler.java
 */

@Slf4j
@Component
public class CircuitBreakerHandler implements HandlerFunction<ServerResponse> {


    @Nonnull
    @Override
    public ServerResponse handle(@Nonnull ServerRequest request) {

        log.error("访问 {} 失败 : 开始启动服务降级机制---->", request.path());
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .contentType(MediaType.APPLICATION_JSON)
                .body(JSON.toJSONString(Result.failure("The current service is unavailable , please try again later !")));

    }


}