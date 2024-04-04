package ptp.fltv.web.handler;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/4 下午 10:21:19
 * @description 负责解决PTP后台中的全部异常(兜底措施, 适用于过滤器内部出现的异常)
 * @filename PtpGlobalExceptionResolver.java
 */

public class PtpGlobalExceptionResolver extends AbstractHandlerExceptionResolver {


    @Override
    protected ModelAndView doResolveException(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, Object handler, @Nonnull Exception ex) {

        return null;

    }


}
