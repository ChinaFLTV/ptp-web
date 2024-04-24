package ptp.fltv.web.security;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import pfp.fltv.common.response.Result;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/2 下午 7:46:28
 * @description 实现默认的认证失败后跳转回登录界面的 {@link AuthenticationEntryPoint }
 * @filename PtpAuthenticationEntryPoint.java
 */

@Slf4j
public class PtpAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        log.error(authException.getCause() == null ? authException.getLocalizedMessage() : authException.getCause().getMessage());

        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();

        Result<?> failResult = Result.failure("权限不够：" + authException.getLocalizedMessage());

        out.write(JSON.toJSONString(failResult));
        out.flush();
        out.close();


    }


}
