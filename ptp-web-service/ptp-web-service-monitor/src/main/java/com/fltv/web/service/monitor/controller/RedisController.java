package com.fltv.web.service.monitor.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.fltv.web.service.monitor.service.RedisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pfp.fltv.common.annotation.LogRecord;
import pfp.fltv.common.model.po.response.Result;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/12/9 PM 9:24:40
 * @description Redis数据库监控控制器
 * @filename RedisController.java
 */

@RequiredArgsConstructor
@Tag(name = "Redis数据库监控接口")
@RestController
@RequestMapping("/monitor/redis")
public class RedisController {


    private final RedisService redisService;


    @LogRecord(description = "查询指定ID的Redis数据库的状态信息")
    @SentinelResource("web-monitor-redis-controller")
    @Operation(description = "查询指定ID的Redis数据库的状态信息")
    @GetMapping("/query/info/{id}")
    public Result<Map<String, Object>> queryInfoById(

            @Parameter(name = "id", description = "待查询的Redis数据库的ID", in = ParameterIn.PATH, required = true) @PathVariable("id") Long id

    ) {

        Map<String, Object> info = redisService.queryInfoById(id);
        return (info == null) ? Result.failure(new HashMap<>()) : Result.success(info);

    }


    @LogRecord(description = "在ID的Redis数据库上执行指定的单条命令")
    @SentinelResource("web-monitor-redis-controller")
    @Operation(description = "在ID的Redis数据库上执行指定的单条命令")
    @PostMapping("/run/single/{id}")
    public Result<Object> runSingleCommand(

            @Parameter(name = "id", description = "待被执行的Redis数据库的ID", in = ParameterIn.PATH, required = true) @PathVariable("id") Long id,
            @Parameter(name = "command", description = "需要被执行的命令(格式要求 : 命令各部分必须且仅能以空格进行分隔(空格数量不限) , 命令结尾不需要添加任何符号)", required = true) @RequestBody String command

    ) {

        return Result.success(redisService.runSingleCommand(id, command));

    }


    @LogRecord(description = "查询指定ID的Redis数据库的全部键值对数据")
    @SentinelResource("web-monitor-redis-controller")
    @Operation(description = "查询指定ID的Redis数据库的全部键值对数据")
    @PostMapping("/query/keyValues/all/{id}")
    public Result<Map<String, Object>> queryAllKeyValuePairsById(

            @Parameter(name = "id", description = "待查询的Redis数据库的ID", in = ParameterIn.PATH, required = true) @PathVariable("id") Long id

    ) {

        Map<String, Object> keyValuePairs = redisService.queryAllKeyValuePairsById(id);
        return (keyValuePairs == null) ? Result.failure(new HashMap<>()) : Result.success(keyValuePairs);

    }


}