package ptp.fltv.web.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import pfp.fltv.common.response.Result;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/16 PM 10:07:26
 * @description 自定义的触发Sentinel流控机制后的处理器
 * @filename SentinelBlockExceptionHandler.java
 */

@Primary
@Component("ptp-web-web-sentinelBlockExceptionHandler") // 2024-5-18  22:13-elasticsearch、minio模块中均含有同名类，加上该注解以解决注入冲突
public class SentinelBlockExceptionHandler implements BlockExceptionHandler {


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException ex) throws Exception {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(503);
        response.getWriter().write(JSON.toJSONString(Result.failure(String.format("The service %s is current unavailable , please try again later !", "ptp-web-service-web"))));

    }


}
