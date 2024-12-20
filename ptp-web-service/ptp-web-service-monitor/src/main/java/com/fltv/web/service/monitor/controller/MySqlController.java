package com.fltv.web.service.monitor.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.fltv.web.service.monitor.model.po.MysqlColumn;
import com.fltv.web.service.monitor.model.po.MysqlTableStatus;
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


    @LogRecord(description = "查询指定ID的MySQL数据库的各个数据库的占用磁盘空间大小")
    @SentinelResource("web-monitor-mysql-controller")
    @Operation(description = "查询指定ID的MySQL数据库的各个数据库的占用磁盘空间大小")
    @GetMapping("/query/database/size/all")
    public Result<Map<String, Long>> queryAllDatabaseSizes(

            @Parameter(name = "id", description = "待查询的MySQL数据库的ID", required = true) @RequestParam("id") Long id,
            @Parameter(name = "count", description = "所要查询的数据库的数量", required = true) @RequestParam("count") Long count

    ) {

        Map<String, Long> sizes = mySqlService.queryAllDatabaseSizes(id, count);
        return (sizes == null) ? Result.failure(new HashMap<>()) : Result.success(sizes);

    }


    @LogRecord(description = "查询指定ID的MySQL数据库的指定/全部数据库下的各张表的占用磁盘空间大小")
    @SentinelResource("web-monitor-mysql-controller")
    @Operation(description = "查询指定ID的MySQL数据库的指定/全部数据库下的各张表的占用磁盘空间大小")
    @GetMapping("/query/table/size")
    public Result<Map<String, Long>> queryAllTableSizes(

            @Parameter(name = "id", description = "待查询的MySQL数据库的ID", required = true) @RequestParam("id") Long id,
            @Parameter(name = "dbName", description = "待查询的数据库名(若想查询全部数据库的表 , 则请将此字段置空)", required = true) @RequestParam("dbName") String dbName,
            @Parameter(name = "count", description = "所要查询的表的数量", required = true) @RequestParam("count") Long count

    ) {

        Map<String, Long> sizes = mySqlService.queryAllTableSizes(id, dbName, count);
        return (sizes == null) ? Result.failure(new HashMap<>()) : Result.success(sizes);

    }


    @LogRecord(description = "查询指定ID的MySQL数据库的单个表的结构信息")
    @SentinelResource("web-monitor-mysql-controller")
    @Operation(description = "查询指定ID的MySQL数据库的单个表的结构信息")
    @GetMapping("/query/table/structure/single")
    public Result<List<MysqlColumn>> querySingleTableStructure(

            @Parameter(name = "id", description = "待查询的MySQL数据库的ID", required = true) @RequestParam("id") Long id,
            @Parameter(name = "tableName", description = "待查询的数据表名", required = true) @RequestParam("tableName") String tableName

    ) {

        List<MysqlColumn> columns = mySqlService.querySingleTableStructure(id, tableName);
        return (columns == null) ? Result.failure(new ArrayList<>()) : Result.success(columns);

    }


    @LogRecord(description = "查询指定ID的MySQL数据库的单个表的状态信息")
    @SentinelResource("web-monitor-mysql-controller")
    @Operation(description = "查询指定ID的MySQL数据库的单个表的状态信息")
    @GetMapping("/query/table/status/single")
    public Result<MysqlTableStatus> querySingleTableStatus(

            @Parameter(name = "id", description = "待查询的MySQL数据库的ID", required = true) @RequestParam("id") Long id,
            @Parameter(name = "tableName", description = "待查询的数据表名", required = true) @RequestParam("tableName") String tableName

    ) {

        MysqlTableStatus tableStatus = mySqlService.querySingleTableStatus(id, tableName);
        return (tableStatus == null) ? Result.failure(null) : Result.success(tableStatus);

    }


    @LogRecord(description = "按照指定的排序规则批量查询指定ID的MySQL数据库的指定表的指定区域的数据")
    @SentinelResource("web-monitor-mysql-controller")
    @Operation(description = "按照指定的排序规则批量查询指定ID的MySQL数据库的指定表的指定区域的数据")
    @GetMapping("/query/table/data/single")
    public Result<List<Map<String, Object>>> querySingleTableData(

            @Parameter(name = "id", description = "待查询的MySQL数据库的ID", required = true) @RequestParam("id") Long id,
            @Parameter(name = "databaseName", description = "待查询的数据库名", required = true) @RequestParam("databaseName") String databaseName,
            @Parameter(name = "tableName", description = "待查询的数据表名", required = true) @RequestParam("tableName") String tableName,
            @Parameter(name = "sortField", description = "用于排序的字段名(不排序请留空)", required = true) @RequestParam("sortField") String sortField,
            @Parameter(name = "asc", description = "是否按照给定的排序字段升序排序(若没有提供排序字段 , 则此参数无效)", required = true) @RequestParam("asc") Boolean asc,
            @Parameter(name = "offset", description = "数据截取的起始偏移量", required = true) @RequestParam("offset") Long offset,
            @Parameter(name = "count", description = "数据截取的数量", required = true) @RequestParam("count") Long count

    ) {

        List<Map<String, Object>> data = mySqlService.querySingleTableData(id, databaseName, tableName, sortField, asc, offset, count);
        return (data == null) ? Result.failure(null) : Result.success(data);

    }


    @LogRecord(description = "更新指定ID的MySQL数据库的指定表的指定单个条目的单个字段的数据")
    @SentinelResource("web-monitor-mysql-controller")
    @Operation(description = "更新指定ID的MySQL数据库的指定表的指定单个条目的单个字段的数据")
    @PutMapping("/update/table/field/data/single")
    public Result<Long> updateSingleTableFieldData(

            @Parameter(name = "id", description = "待查询的MySQL数据库的ID", required = true) @RequestParam("id") Long id,
            @Parameter(name = "databaseName", description = "待操作的数据库名", required = true) @RequestParam("databaseName") String databaseName,
            @Parameter(name = "tableName", description = "待操作的数据表名", required = true) @RequestParam("tableName") String tableName,
            @Parameter(name = "modelId", description = "待更新的条目的主键ID(请确保该ID能唯一标识一个条目)", required = true) @RequestParam("modelId") Long modelId,
            @Parameter(name = "fieldName", description = "待更新的字段名", required = true) @RequestParam("fieldName") String fieldName,
            @Parameter(name = "fieldValue", description = "待更新的字段值(可为空值)", required = true) @RequestParam("fieldValue") String fieldValue

    ) {

        boolean isUpdated = mySqlService.updateSingleTableFieldData(id, databaseName, tableName, modelId, fieldName, fieldValue);
        return isUpdated ? Result.success(modelId) : Result.failure(-1L);

    }


    @LogRecord(description = "删除指定ID的MySQL数据库的指定表的指定单个条目")
    @SentinelResource("web-monitor-mysql-controller")
    @Operation(description = "删除指定ID的MySQL数据库的指定表的指定单个条目")
    @DeleteMapping("/delete/table/data/single")
    public Result<Long> deleteSingleTableData(

            @Parameter(name = "id", description = "待操作的MySQL数据库的ID", required = true) @RequestParam("id") Long id,
            @Parameter(name = "databaseName", description = "待操作的数据库名", required = true) @RequestParam("databaseName") String databaseName,
            @Parameter(name = "tableName", description = "待操作的数据表名", required = true) @RequestParam("tableName") String tableName,
            @Parameter(name = "modelId", description = "待删除的条目的主键ID(请确保该ID能唯一标识一个条目)", required = true) @RequestParam("modelId") Long modelId

    ) {

        boolean isDeleted = mySqlService.deleteSingleTableData(id, databaseName, tableName, modelId);
        return isDeleted ? Result.success(modelId) : Result.failure(-1L);

    }


}