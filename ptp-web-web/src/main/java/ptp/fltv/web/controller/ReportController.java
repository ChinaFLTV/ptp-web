package ptp.fltv.web.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pfp.fltv.common.annotation.LogRecord;
import pfp.fltv.common.model.po.manage.Report;
import pfp.fltv.common.model.po.response.Result;
import ptp.fltv.web.service.ReportService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/10/17 PM 5:55:27
 * @description 举报控制器
 * @filename ReportController.java
 */

@AllArgsConstructor
@Tag(name = "举报操作接口")
@RestController
@RequestMapping("/manage/report")
public class ReportController {


    private ReportService reportService;


    @LogRecord(description = "根据ID查询单条举报数据")
    @SentinelResource("web-content-report-controller")
    @Operation(description = "根据ID查询单条举报数据")
    @GetMapping("/query/single/{id}")
    public Result<Report> querySingleReport(@PathVariable("id") Long id) {

        Report report = reportService.getById(id);
        return (report == null) ? Result.failure(null) : Result.success(report);

    }


    @LogRecord(description = "批量(分页)查询多条举报数据")
    @SentinelResource("web-content-report-controller")
    @Operation(description = "批量(分页)查询多条举报数据")
    @GetMapping("/query/page/{offset}/{limit}")
    public Result<List<Report>> queryReportPage(

            @Parameter(name = "offset", description = "查询的一页举报数据的起始偏移量", in = ParameterIn.PATH, required = true) @PathVariable("offset") Long offset,
            @Parameter(name = "limit", description = "查询的这一页举报数据的数量", in = ParameterIn.PATH, required = true) @PathVariable("limit") Long limit

    ) {

        Page<Report> reportPage = new Page<>(offset, limit);
        reportPage = reportService.page(reportPage);

        return Result.success(reportPage.getRecords() == null ? new ArrayList<>() : reportPage.getRecords());

    }


    @GlobalTransactional(name = "insert-single-report", rollbackFor = Exception.class)
    @LogRecord(description = "添加单条举报数据")
    @SentinelResource("web-content-report-controller")
    @Operation(description = "添加单条举报数据")
    @PostMapping("/insert/single")
    public Result<Long> insertSingleReport(

            @Parameter(name = "report", description = "待添加的单条举报数据", required = true) @RequestBody Report report

    ) {

        boolean isSaved = reportService.save(report);
        return isSaved ? Result.success(report.getId()) : Result.failure(-1L);

    }


    @GlobalTransactional(name = "update-single-report", rollbackFor = Exception.class)
    @LogRecord(description = "修改单条举报数据")
    @SentinelResource("web-content-report-controller")
    @Operation(description = "修改单条举报数据")
    @PutMapping("/update/single")
    public Result<?> updateSingleReport(@RequestBody Report report) {

        boolean isUpdated = reportService.updateById(report);
        return isUpdated ? Result.success(null) : Result.failure(null);

    }


    @GlobalTransactional(name = "delete-single-report", rollbackFor = Exception.class)
    @LogRecord(description = "删除单条举报数据")
    @SentinelResource("web-content-report-controller")
    @Operation(description = "删除单条举报数据")
    @DeleteMapping("/delete/single/{id}")
    public Result<?> deleteSingleReport(@PathVariable("id") Long id) {

        boolean isDeleted = reportService.removeById(id);
        return isDeleted ? Result.success(null) : Result.failure(null);

    }


}