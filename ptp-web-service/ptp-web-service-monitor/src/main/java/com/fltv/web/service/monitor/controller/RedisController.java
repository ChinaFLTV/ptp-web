package com.fltv.web.service.monitor.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.fltv.web.service.monitor.model.po.RedisKeyValueInfo;
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
    @GetMapping("/query/keyValues/all/{id}")
    public Result<Map<String, Object>> queryAllKeyValuePairsById(

            @Parameter(name = "id", description = "待查询的Redis数据库的ID", in = ParameterIn.PATH, required = true) @PathVariable("id") Long id

    ) {

        Map<String, Object> keyValuePairs = redisService.queryAllKeyValuePairsById(id);
        return (keyValuePairs == null) ? Result.failure(new HashMap<>()) : Result.success(keyValuePairs);

    }


    @LogRecord(description = "查询指定ID的Redis数据库的单个键值对的详细信息")
    @SentinelResource("web-monitor-redis-controller")
    @Operation(description = "查询指定ID的Redis数据库的单个键值对的详细信息")
    @GetMapping("/query/keyValues/single")
    public Result<RedisKeyValueInfo> querySingleKeyValuePairInfo(

            @Parameter(name = "id", description = "待查询的Redis数据库的ID", required = true) @RequestParam("id") Long id,
            @Parameter(name = "key", description = "待查询的单个Redis键", required = true) @RequestParam("key") String key

    ) {

        RedisKeyValueInfo info = redisService.querySingleKeyValuePairInfo(id, key);
        return (info == null) ? Result.failure(null) : Result.success(info);

    }


    @LogRecord(description = "添加指定ID的Redis数据库指定的单个键值对")
    @SentinelResource("web-monitor-redis-controller")
    @Operation(description = "添加指定ID的Redis数据库指定的单个键值对")
    @PostMapping("/insert/keyValues/single")
    public Result<Boolean> insertSingleKeyValuePair(

            @Parameter(name = "id", description = "待操作的Redis数据库的ID", required = true) @RequestParam("id") Long id,
            @Parameter(name = "info", description = "添加后的单个键值对信息数据包(key、value、ttl字段必须填写)", required = true) @RequestBody RedisKeyValueInfo info

    ) {

        boolean isInserted = redisService.insertSingleKeyValuePair(id, info);
        return isInserted ? Result.success(true) : Result.failure(null);

    }


    @LogRecord(description = "更新指定ID的Redis数据库指定的单个键值对")
    @SentinelResource("web-monitor-redis-controller")
    @Operation(description = "更新指定ID的Redis数据库指定的单个键值对")
    @PutMapping("/update/keyValues/single")
    public Result<Boolean> updateSingleKeyValuePair(

            @Parameter(name = "id", description = "待操作的Redis数据库的ID", required = true) @RequestParam("id") Long id,
            @Parameter(name = "oldKey", description = "待更新的单个Redis键(旧键名)", required = true) @RequestParam("oldKey") String oldKey,
            @Parameter(name = "info", description = "更新后的单个键值对信息数据包(key、value、ttl字段必须填写)", required = true) @RequestBody RedisKeyValueInfo info


    ) {

        boolean isUpdated = redisService.updateSingleKeyValuePair(id, oldKey, info);
        return isUpdated ? Result.success(true) : Result.failure(null);

    }


    @LogRecord(description = "删除指定ID的Redis数据库指定的单个键值对")
    @SentinelResource("web-monitor-redis-controller")
    @Operation(description = "删除指定ID的Redis数据库指定的单个键值对")
    @DeleteMapping("/delete/keyValues/single")
    public Result<Long> deleteSingleKeyValuePair(

            @Parameter(name = "id", description = "待操作的Redis数据库的ID", required = true) @RequestParam("id") Long id,
            @Parameter(name = "key", description = "待删除的单个Redis键", required = true) @RequestParam("key") String key

    ) {

        long res = redisService.deleteSingleKeyValuePair(id, key);
        return res > 0 ? Result.success(res) : Result.failure(res);

    }


}