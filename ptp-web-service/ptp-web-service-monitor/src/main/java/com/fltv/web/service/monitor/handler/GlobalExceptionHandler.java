package com.fltv.web.service.monitor.handler;

import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pfp.fltv.common.exceptions.PtpException;
import pfp.fltv.common.model.po.response.Result;

import javax.sql.DataSource;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/26 下午 7:00:14
 * @description 对业务逻辑中产生的异常进行捕获处理的控制器
 * @filename GlobalExceptionHandler.java
 */

@RequiredArgsConstructor
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    private final DataSource dataSource;


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/12/16 PM 5:11:14
     * @version 1.0.0
     * @description 专门捕获因通过Nacos进行配置更新而导致Druid数据库连接池被销毁而没有正常重建进而导致获取在通过MyBatisPlus进行数据操作时报 org.springframework.jdbc.CannotGetJdbcConnectionException: Failed to obtain JDBC Connection 异常
     * @filename GlobalExceptionHandler.java
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MyBatisSystemException.class)
    public Result<String> handleSqlException(MyBatisSystemException ex) throws Exception {

        log.error("[{}] : {} occurred : {}", "ptp-web-service-monitor", "MyBatisSystemException", ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());

        if (ex.getCause() != null) {

            // 2024-12-16  22:23-因为能抛出 MyBatisSystemException 异常的情况还有很多种 , 我们这里不能一刀切式的去直接销毁&重启数据库 , 这种操作代价太高 , 因此这里更具体一些 , 只针对JDBC连接无法获取的异常进行处理
            if (ex.getCause().getLocalizedMessage().contains("org.springframework.jdbc.CannotGetJdbcConnectionException: Failed to obtain JDBC Connection")) {

                log.error("当前数据库连接池已损坏 , 将启动数据库 关闭再重启 工作...");
                ((DruidDataSourceWrapper) dataSource).destroy();
                ((DruidDataSourceWrapper) dataSource).restart();

                return Result.failure("数据库连接池重建完毕 , 请尝试再次发送请求来继续刚刚未完成的业务!");

            }

        }

        //// 2024-12-16  22:36-如果该异常在本次捕获控制器中未作处理 , 则继续抛出交由其他处理器进行捕获处理
        return Result.failure(ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());

    }


    /**
     * @param ex 产生的异常
     * @return 给前端的失败信息
     * @author Lenovo/LiGuanda
     * @date 2024/4/7 下午 10:41:19
     * @version 1.0.0
     * @description 捕获全局的PtpException异常(该异常一般由用户填写的数据非法导致的)
     * @filename PtpExceptionHandler.java
     */
    // 2024-9-29  15:30-抛出异常所属的父类为PTPException , 则说明该异常的出现并非原始错误 , 而是业务出错 , 因此响应码不会置为4XX , 而改写为响应体中返回给前端去进行个性化处理
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PtpException.class)
    public Result<String> handlePtpException(PtpException ex) {

        log.error("[{}] : {} occurred : {} [{}]", "ptp-web-service-monitor", "Exception", ex.getDetailedMessage(), ex.getCode());
        Tracer.trace(ex);// 2024-5-17  20:29-上报异常信息到Sentinel

        return Result.failure(ex.getDetailedMessage());

    }


    /**
     * @param ex 产生的异常
     * @return 给前端的失败信息
     * @author Lenovo/LiGuanda
     * @date 2024/5/18 PM 11:00:23
     * @version 1.0.0
     * @description 捕获全局的PtpException异常(该异常一般由Sentinel在触发流控机制后抛出)
     * @filename PtpExceptionHandler.java
     */
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    @ExceptionHandler(FlowException.class)
    public Result<String> handleFlowException(FlowException ex) {

        log.error("[{}] : {} occurred : {}", "ptp-web-service-monitor", "FlowException", ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());
        Tracer.trace(ex);

        return Result.failure(ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());

    }


    /**
     * @param ex 产生的异常
     * @return 给前端的失败信息
     * @author Lenovo/LiGuanda
     * @date 2024/3/26 下午 7:10:23
     * @version 1.0.0
     * @description 捕获全局异常
     * @filename PtpExceptionHandler.java
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception ex) {

        log.error("[{}] : {} occurred : {}", "ptp-web-service-monitor", ex.getClass().getName(), ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());
        Tracer.trace(ex);// 2024-5-17  20:31-上报异常信息到Sentinel
        return Result.failure(ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());

    }


}