package com.fltv.web.service.monitor.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.fltv.web.service.monitor.model.po.ProcessListEntry;
import com.fltv.web.service.monitor.model.po.TableInfo;
import com.fltv.web.service.monitor.service.MySqlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pfp.fltv.common.annotation.LogRecord;
import pfp.fltv.common.model.po.response.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/12/9 PM 3:22:36
 * @description MySQL数据库监控控制器
 * @filename MySqlController.java
 */

@RequiredArgsConstructor
@Tag(name = "MySQL数据库监控接口")
@RestController
@RequestMapping("/monitor/mysql")
public class MySqlController {


    private final MySqlService mySqlService;


    @LogRecord(description = "查询指定ID的MySQL数据库的连接状态信息")
    @SentinelResource("web-monitor-mysql-controller")
    @Operation(description = "查询指定ID的MySQL数据库的连接状态信息")
    @GetMapping("/query/connections/{id}")
    public Result<Map<String, Integer>> queryConnectionsById(

            @Parameter(name = "id", description = "待查询的MySQL数据库的ID", in = ParameterIn.PATH, required = true) @PathVariable("id") Long id

    ) {

        Map<String, Integer> connectionsStatistics = mySqlService.getConnectionsStatistics(id);
        return (connectionsStatistics == null) ? Result.failure(new HashMap<>()) : Result.success(connectionsStatistics);

    }


    @LogRecord(description = "查询指定ID的MySQL数据库的当前活跃的连接的详细状态信息列表")
    @SentinelResource("web-monitor-mysql-controller")
    @Operation(description = "查询指定ID的MySQL数据库的当前活跃的连接的详细状态信息列表")
    @GetMapping("/query/processes/{id}")
    public Result<List<ProcessListEntry>> queryProcessesById(

            @Parameter(name = "id", description = "待查询的MySQL数据库的ID", in = ParameterIn.PATH, required = true) @PathVariable("id") Long id

    ) {

        List<ProcessListEntry> processList = mySqlService.getFullProcessListById(id);
        return (processList == null) ? Result.failure(new ArrayList<>()) : Result.success(processList);

    }


    @LogRecord(description = "查询指定ID的MySQL数据库的状态信息")
    @SentinelResource("web-monitor-mysql-controller")
    @Operation(description = "查询指定ID的MySQL数据库的状态信息")
    @GetMapping("/query/status/{id}")
    public Result<Map<String, Object>> queryStatusById(

            @Parameter(name = "id", description = "待查询的MySQL数据库的ID", in = ParameterIn.PATH, required = true) @PathVariable("id") Long id

    ) {

        Map<String, Object> status = mySqlService.getStatusById(id);
        return (status == null) ? Result.failure(new HashMap<>()) : Result.success(status);

    }


    @LogRecord(description = "查询指定ID的MySQL数据库的系统变量信息")
    @SentinelResource("web-monitor-mysql-controller")
    @Operation(description = "查询指定ID的MySQL数据库的系统变量信息")
    @GetMapping("/query/variables/{id}")
    public Result<Map<String, Object>> queryVariablesById(

            @Parameter(name = "id", description = "待查询的MySQL数据库的ID", in = ParameterIn.PATH, required = true) @PathVariable("id") Long id

    ) {

        Map<String, Object> variables = mySqlService.getVariablesById(id);
        return (variables == null) ? Result.failure(new HashMap<>()) : Result.success(variables);

    }


    @LogRecord(description = "查询指定ID的MySQL数据库的基本的状态信息")
    @SentinelResource("web-monitor-mysql-controller")
    @Operation(description = "查询指定ID的MySQL数据库的基本的状态信息")
    @GetMapping("/query/status/base/{id}")
    public Result<Map<String, Object>> queryBaseStatusById(

            @Parameter(name = "id", description = "待查询的MySQL数据库的ID", in = ParameterIn.PATH, required = true) @PathVariable("id") Long id

    ) throws InterruptedException {

        Map<String, Object> status = mySqlService.getBaseStatusById(id);
        return (status == null) ? Result.failure(new HashMap<>()) : Result.success(status);

    }


    @LogRecord(description = "查询指定ID的MySQL数据库的指定数据库的全部表的信息")
    @SentinelResource("web-monitor-mysql-controller")
    @Operation(description = "查询指定ID的MySQL数据库的指定数据库的全部表的信息")
    @GetMapping("/query/table/info/all")
    public Result<List<TableInfo>> queryAllTableInfoInTargetDatabase(

            @Parameter(name = "id", description = "待查询的MySQL数据库的ID", required = true) @RequestParam("id") Long id,
            @Parameter(name = "database", description = "待查询的数据库名", required = true) @RequestParam("database") String database

    ) {

        List<TableInfo> infos = mySqlService.queryAllTableInfoInTargetDatabase(id, database);
        return (infos == null) ? Result.failure(new ArrayList<>()) : Result.success(infos);

    }


}