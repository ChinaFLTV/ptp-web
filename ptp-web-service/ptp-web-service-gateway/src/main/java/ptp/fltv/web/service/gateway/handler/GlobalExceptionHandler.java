package ptp.fltv.web.service.gateway.handler;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import pfp.fltv.common.model.po.response.Result;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/18 PM 8:31:32
 * @description 自定义的网关全局异常处理器
 * @filename GlobalExceptionHandler.java
 */

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {


    @Nonnull
    @Override
    public Mono<Void> handle(@Nonnull ServerWebExchange exchange, @Nonnull Throwable ex) {

        ServerHttpResponse response = exchange.getResponse();

        String exMsg = ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage();

        log.error(exMsg);

        Result<String> failureResult = Result.failure(exMsg);
        DataBuffer dataBuffer = response.bufferFactory().wrap(JSON.toJSONString(failureResult).getBytes(StandardCharsets.UTF_8));

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);

        return response.writeWith(Mono.just(dataBuffer));

    }


}