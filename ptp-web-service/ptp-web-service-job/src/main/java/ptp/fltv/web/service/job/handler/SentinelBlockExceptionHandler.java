package ptp.fltv.web.service.job.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import pfp.fltv.common.model.po.response.Result;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/19 PM 9:22:04
 * @description 自定义的触发Sentinel流控机制后的处理器
 * @filename SentinelBlockExceptionHandler.java
 */

@Component
public class SentinelBlockExceptionHandler implements BlockExceptionHandler {


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException ex) throws Exception {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(503);
        response.getWriter().write(JSON.toJSONString(Result.failure(String.format("The service %s is current unavailable , please try again later !", "ptp-web-service-job"))));

    }


}
