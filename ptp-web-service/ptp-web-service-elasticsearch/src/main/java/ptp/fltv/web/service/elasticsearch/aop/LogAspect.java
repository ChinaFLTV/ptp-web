package ptp.fltv.web.service.elasticsearch.aop;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pfp.fltv.common.annotation.LogRecord;

import java.util.Arrays;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/16 PM 10:05:39
 * @description 通过AOP的方式对方法的执行情况以日志的形式进行记录
 * 主文件请更新完Web服务模块下的aop包中的LogAspect类之后，再来同步更新此文件
 * @filename LogAspect.java
 */

@Aspect
@Component
public class LogAspect {


    @Pointcut("@annotation(pfp.fltv.common.annotation.LogRecord)")
    private void needLogRecordMethods() {


    }


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/6/16 PM 10:25:29
     * @version 1.0.0
     * @description 增强方法(该方法建议用在Web环境下)，这里我们采用{@code @Around}来记录日志而不是{@code @After}注解，
     * 是因为我们需要根据方法执行的成功与否来决定如何输出日志，而{@code @After}无法知晓方法是否执行成功
     * @filename LogAspect.java
     */
    @Around("needLogRecordMethods()")
    public Object logRecord(ProceedingJoinPoint joinPoint) throws Throwable {

        Class<?> clazz = joinPoint.getTarget().getClass();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        LogRecord logRecord = methodSignature.getMethod().getAnnotation(LogRecord.class);

        Logger logger = LoggerFactory.getLogger(clazz);

        MDC.put("method-description", logRecord.description());


        if (RequestContextHolder.getRequestAttributes() != null) {

            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String requestUrl = request.getRequestURL().toString();
            String methodType = request.getMethod();
            String ip = request.getRemoteAddr();
            String protocol = request.getProtocol();
            Map<String, String[]> parameterMap = request.getParameterMap();

            logger.info("IP为 [{}] 的用户向 服务[{}] 发来了 {} 类型的 {} 请求 : 受理服务({})", ip, requestUrl, protocol, methodType, clazz.getName() + "::" + methodSignature.getName());
            if (!parameterMap.isEmpty()) {

                StringBuilder builder = new StringBuilder();
                for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {

                    builder.append(entry.getKey())
                            .append(" : ")
                            .append(Arrays.toString(entry.getValue()))
                            .append("\n");

                }
                logger.info("请求的query参数 : \n{}", builder);

            }

        } else {

            logger.info("受理服务 [ {} ] 接收到了一个用户请求", clazz.getName() + "::" + methodSignature.getName());
            logger.info("受理服务 [ {} ] 入参情况 : {}", clazz.getName() + "::" + methodSignature.getName(), Arrays.toString(joinPoint.getArgs()));

        }

        Object result;
        try {

            result = joinPoint.proceed();
            logger.info("受理服务 [ {} ] 执行完成,执行结果 = {}", clazz.getName() + "::" + methodSignature.getName(), result);

        } catch (Throwable ex) {

            logger.error("受理服务 [ {} ] 执行失败,异常信息 = {}", clazz.getName() + "::" + methodSignature.getName(), ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());
            throw ex;

        } finally {

            MDC.remove("method-description");

        }

        return result;

    }


}