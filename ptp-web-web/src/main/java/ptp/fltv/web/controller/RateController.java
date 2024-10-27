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
import pfp.fltv.common.model.po.manage.Rate;
import pfp.fltv.common.model.po.response.Result;
import ptp.fltv.web.service.RateService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/10/26 AM 12:23:04
 * @description 评分控制器
 * @filename RateController.java
 */

@AllArgsConstructor
@Tag(name = "评分操作接口")
@RestController
@RequestMapping("/manage/rate")
public class RateController {


    private RateService rateService;


    @LogRecord(description = "根据ID查询单条内容分统计数据")
    @SentinelResource("web-manage-rate-controller")
    @Operation(description = "根据ID查询单条内容评分统计数据")
    @GetMapping("/query/single/statistic/{id}")
    public Result<Rate> querySingleContentRate(@PathVariable("id") Long id) {

        Rate rate = rateService.getById(id);
        return (rate == null) ? Result.failure(null) : Result.success(rate);

    }


    @LogRecord(description = "根据内容ID和用户ID去查询用户在某个内容的评分记录数据")
    @SentinelResource("web-manage-rate-controller")
    @Operation(description = "根据内容ID和用户ID去查询用户在某个内容的评分记录数据")
    @GetMapping("/query/single")
    public Result<Rate> querySingleUserRate(

            @Parameter(name = "contentId", description = "内容实体的ID", required = true) @RequestParam("contentId") Long contentId,
            @Parameter(name = "uid", description = "评分用户的ID", required = true) @RequestParam("uid") Long uid

    ) {

        Rate rate = rateService.querySingleUserRate(contentId, uid);
        return (rate == null) ? Result.failure(null) : Result.success(rate);

    }


    @LogRecord(description = "批量(分页)查询多条评分数据")
    @SentinelResource("web-manage-rate-controller")
    @Operation(description = "批量(分页)查询多条评分数据")
    @GetMapping("/query/page/{offset}/{limit}")
    public Result<List<Rate>> queryRatePage(

            @Parameter(name = "offset", description = "查询的一页评分数据的起始偏移量", in = ParameterIn.PATH, required = true) @PathVariable("offset") Long offset, @Parameter(name = "limit", description = "查询的这一页评分数据的数量", in = ParameterIn.PATH, required = true) @PathVariable("limit") Long limit

    ) {

        Page<Rate> ratePage = new Page<>(offset, limit);
        ratePage = rateService.page(ratePage);

        return Result.success(ratePage.getRecords() == null ? new ArrayList<>() : ratePage.getRecords());

    }


    @GlobalTransactional(name = "insert-single-rate", rollbackFor = Exception.class)
    @LogRecord(description = "添加单条评分数据")
    @SentinelResource("web-manage-rate-controller")
    @Operation(description = "添加单条评分数据")
    @PostMapping("/insert/single")
    public Result<Long> insertSingleRate(

            @Parameter(name = "rate", description = "待添加的单条评分数据", required = true) @RequestBody Rate rate

    ) {

        boolean isSaved = rateService.insertSingleRate(rate);
        return isSaved ? Result.success(rate.getId()) : Result.failure(-1L);

    }


    @GlobalTransactional(name = "update-single-rate", rollbackFor = Exception.class)
    @LogRecord(description = "修改单条评分数据")
    @SentinelResource("web-manage-rate-controller")
    @Operation(description = "修改单条评分数据")
    @PutMapping("/update/single")
    public Result<?> updateSingleRate(@RequestBody Rate rate) {

        boolean isUpdated = rateService.updateById(rate);
        return isUpdated ? Result.success(null) : Result.failure(null);

    }


    @GlobalTransactional(name = "delete-single-rate", rollbackFor = Exception.class)
    @LogRecord(description = "删除单条评分数据")
    @SentinelResource("web-manage-rate-controller")
    @Operation(description = "删除单条评分数据")
    @DeleteMapping("/delete/single/{id}")
    public Result<?> deleteSingleRate(@PathVariable("id") Long id) {

        boolean isDeleted = rateService.removeById(id);
        return isDeleted ? Result.success(null) : Result.failure(null);

    }


}