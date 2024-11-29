package ptp.fltv.web.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pfp.fltv.common.annotation.LogRecord;
import pfp.fltv.common.enums.ContentStatus;
import pfp.fltv.common.model.po.agriculture.AgricultureEnvironment;
import pfp.fltv.common.model.po.response.Result;
import ptp.fltv.web.service.AgricultureEnvironmentService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/11/29 PM 8:54:53
 * @description 农业生产环境数据控制器
 * @filename AgricultureEnvironmentController.java
 */

@AllArgsConstructor
@Tag(name = "农业生产环境数据操作接口")
@RestController
@RequestMapping("/agriculture/environment")
public class AgricultureEnvironmentController {


    private AgricultureEnvironmentService agricultureEnvironmentService;


    @LogRecord(description = "根据ID查询单条农业生产环境数据")
    @SentinelResource("web-agriculture-environment-controller")
    @Operation(description = "根据ID查询单条农业生产环境数据")
    @GetMapping("/query/single/{id}")
    public Result<AgricultureEnvironment> querySingleAgricultureEnvironment(

            @Parameter(name = "id", description = "待查询的单条农业生产环境数据ID", in = ParameterIn.PATH, required = true) @PathVariable("id") Long id

    ) {

        AgricultureEnvironment agricultureEnvironment = agricultureEnvironmentService.getById(id);
        return (agricultureEnvironment == null) ? Result.failure(null) : Result.success(agricultureEnvironment);

    }


    @LogRecord(description = "根据节点ID查询该节点的最新一条单条农业生产环境数据")
    @SentinelResource("web-agriculture-environment-controller")
    @Operation(description = "根据节点ID查询该节点的最新一条单条农业生产环境数据")
    @GetMapping("/queryLatestSingleAgricultureEnvironmentByNodeId")
    public Result<AgricultureEnvironment> queryLatestSingleAgricultureEnvironmentByNodeId(

            @Parameter(name = "nodeId", description = "需要查询最新一条农业生产环境数据的节点ID(-1则表示查询全部节点的状态数据)", required = true) @RequestParam("nodeId") Long nodeId

    ) {

        AgricultureEnvironment agricultureEnvironment = agricultureEnvironmentService.queryLatestSingleAgricultureEnvironmentByNodeId(nodeId);
        return (agricultureEnvironment == null) ? Result.failure(null) : Result.success(agricultureEnvironment);

    }


    @LogRecord(description = "批量(分页)查询多条农业生产环境数据(对实体可见状态不做限制)")
    @SentinelResource("web-agriculture-environment-controller")
    @Operation(description = "批量(分页)查询多条农业生产环境数据(对实体可见状态不做限制)")
    @GetMapping("/query/page/{pageNum}/{pageSize}")
    public Result<List<AgricultureEnvironment>> queryAgricultureEnvironmentPage(

            @Parameter(name = "pageNum", description = "查询的一页农业生产环境数据的数据页页码", in = ParameterIn.PATH, required = true) @PathVariable("pageNum") Long pageNum,
            @Parameter(name = "pageSize", description = "查询的这一页农业生产环境数据的数量", in = ParameterIn.PATH, required = true) @PathVariable("pageSize") Long pageSize

    ) {

        Page<AgricultureEnvironment> agricultureEnvironmentPage = new Page<>(pageNum, pageSize);
        agricultureEnvironmentPage = agricultureEnvironmentService.page(agricultureEnvironmentPage);

        return Result.success(agricultureEnvironmentPage.getRecords() == null ? new ArrayList<>() : agricultureEnvironmentPage.getRecords());

    }


    @LogRecord(description = "批量根据指定排序规则(分页)查询多条农业生产环境数据(对实体可见状态不做限制)")
    @SentinelResource("web-agriculture-environment-controller")
    @Operation(description = "批量根据指定排序规则(分页)查询多条农业生产环境数据(对实体可见状态不做限制)")
    @GetMapping("/queryAgricultureEnvironmentPageByNodeIdWithSorting")
    public Result<List<AgricultureEnvironment>> queryAgricultureEnvironmentPageByNodeIdWithSorting(

            @Parameter(name = "nodeId", description = "分组查询的一页农业生产环境数据的数据页页码", required = true) @RequestParam("nodeId") Long nodeId,
            @Parameter(name = "sortType", description = "分组查询的一页农业生产环境数据的排序规则", required = true) @RequestParam("sortType") AgricultureEnvironment.SortType sortType,
            @Parameter(name = "pageNum", description = "查询的一页农业生产环境数据的数据页页码", required = true) @RequestParam("pageNum") Long pageNum,
            @Parameter(name = "pageSize", description = "查询的这一页农业生产环境数据的数量", required = true) @RequestParam("pageSize") Long pageSize

    ) {

        List<AgricultureEnvironment> agricultureEnvironments = agricultureEnvironmentService.queryAgricultureEnvironmentPageByNodeIdWithSorting(nodeId, sortType, pageNum, pageSize);

        return Result.success(agricultureEnvironments == null ? new ArrayList<>() : agricultureEnvironments);

    }


    @LogRecord(description = "查询全部可见的农业生产环境数据")
    @SentinelResource("web-agriculture-environment-controller")
    @Operation(description = "查询全部可见的农业生产环境数据")
    @GetMapping("/query/all/available")
    public Result<List<AgricultureEnvironment>> queryAllAgricultureEnvironments() {

        QueryWrapper<AgricultureEnvironment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", ContentStatus.NORMAL.getCode());
        List<AgricultureEnvironment> agricultureEnvironments = agricultureEnvironmentService.list(queryWrapper);
        return Result.success(agricultureEnvironments);

    }


    @GlobalTransactional(name = "insert-single-banner", rollbackFor = Exception.class)
    @LogRecord(description = "添加单条农业生产环境数据")
    @SentinelResource("web-agriculture-environment-controller")
    @Operation(description = "添加单条农业生产环境数据")
    @PostMapping("/insert/single")
    public Result<Long> insertSingleAgricultureEnvironment(

            @Parameter(name = "agricultureEnvironment", description = "待添加的单条农业生产环境数据", required = true) @RequestBody AgricultureEnvironment agricultureEnvironment

    ) {

        boolean isSaved = agricultureEnvironmentService.save(agricultureEnvironment);
        return isSaved ? Result.success(agricultureEnvironment.getId()) : Result.failure(-1L);

    }


    @GlobalTransactional(name = "update-single-banner", rollbackFor = Exception.class)
    @LogRecord(description = "修改单条农业生产环境数据")
    @SentinelResource("web-agriculture-environment-controller")
    @Operation(description = "修改单条农业生产环境数据")
    @PutMapping("/update/single")
    public Result<?> updateSingleAgricultureEnvironment(@RequestBody AgricultureEnvironment agricultureEnvironment) {

        boolean isUpdated = agricultureEnvironmentService.updateById(agricultureEnvironment);
        return isUpdated ? Result.success(null) : Result.failure(null);

    }


    @GlobalTransactional(name = "delete-single-banner", rollbackFor = Exception.class)
    @LogRecord(description = "删除单条农业生产环境数据")
    @SentinelResource("web-agriculture-environment-controller")
    @Operation(description = "删除单条农业生产环境数据")
    @DeleteMapping("/delete/single/{id}")
    public Result<?> deleteSingleAgricultureEnvironment(@PathVariable("id") Long id) {

        boolean isDeleted = agricultureEnvironmentService.removeById(id);
        return isDeleted ? Result.success(null) : Result.failure(null);

    }


}