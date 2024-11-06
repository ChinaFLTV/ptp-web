package ptp.fltv.web.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pfp.fltv.common.annotation.LogRecord;
import pfp.fltv.common.enums.ContentStatus;
import pfp.fltv.common.model.po.info.UpdateInfo;
import pfp.fltv.common.model.po.response.Result;
import ptp.fltv.web.service.UpdateInfoService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/11/6 PM 11:48:54
 * @description 更新信息控制器
 * @filename UpdateInfoController.java
 */

@RequiredArgsConstructor
@Tag(name = "更新信息操作接口")
@RestController
@RequestMapping("/info/update")
public class UpdateInfoController {


    private final UpdateInfoService updateInfoService;


    @LogRecord(description = "根据ID查询单条更新信息数据")
    @SentinelResource("web-info-update-controller")
    @Operation(description = "根据ID查询单条更新信息数据")
    @GetMapping("/query/single/{id}")
    public Result<UpdateInfo> querySingleUpdateInfo(@PathVariable("id") Long id) {

        UpdateInfo updateInfo = updateInfoService.getById(id);
        return (updateInfo == null) ? Result.failure(null) : Result.success(updateInfo);

    }


    @LogRecord(description = "查询最新的单条更新信息数据")
    @SentinelResource("web-info-update-controller")
    @Operation(description = "查询最新的单条更新信息数据")
    @GetMapping("/query/single/latest")
    public Result<UpdateInfo> queryLatestSingleUpdateInfo() {

        UpdateInfo updateInfo = updateInfoService.queryLatestSingleUpdateInfo();
        return Result.success(updateInfo);

    }


    @LogRecord(description = "批量(分页)查询多条更新信息数据(对实体可见状态不做限制)")
    @SentinelResource("web-info-update-controller")
    @Operation(description = "批量(分页)查询多条更新信息数据(对实体可见状态不做限制)")
    @GetMapping("/query/page/{offset}/{limit}")
    public Result<List<UpdateInfo>> queryUpdateInfoPage(

            @Parameter(name = "offset", description = "查询的一页更新信息数据的起始偏移量", in = ParameterIn.PATH, required = true) @PathVariable("offset") Long offset,
            @Parameter(name = "limit", description = "查询的这一页更新信息数据的数量", in = ParameterIn.PATH, required = true) @PathVariable("limit") Long limit

    ) {

        Page<UpdateInfo> updateInfoPage = new Page<>(offset, limit);
        updateInfoPage = updateInfoService.page(updateInfoPage);

        return Result.success(updateInfoPage.getRecords() == null ? new ArrayList<>() : updateInfoPage.getRecords());

    }


    @LogRecord(description = "查询全部可见的更新信息数据")
    @SentinelResource("web-info-update-controller")
    @Operation(description = "查询全部可见的更新信息数据")
    @GetMapping("/query/all/available")
    public Result<List<UpdateInfo>> queryAllUpdateInfos() {

        QueryWrapper<UpdateInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", ContentStatus.NORMAL.getCode());
        List<UpdateInfo> updateInfos = updateInfoService.list(queryWrapper);
        return Result.success(updateInfos);

    }


    @GlobalTransactional(name = "insert-single-updateInfo", rollbackFor = Exception.class)
    @LogRecord(description = "添加单条更新信息数据")
    @SentinelResource("web-info-update-controller")
    @Operation(description = "添加单条更新信息数据")
    @PostMapping("/insert/single")
    public Result<Long> insertSingleUpdateInfo(

            @Parameter(name = "updateInfo", description = "待添加的单条更新信息数据", required = true) @RequestBody UpdateInfo updateInfo

    ) {

        boolean isSaved = updateInfoService.save(updateInfo);
        return isSaved ? Result.success(updateInfo.getId()) : Result.failure(-1L);

    }


    @GlobalTransactional(name = "update-single-updateInfo", rollbackFor = Exception.class)
    @LogRecord(description = "修改单条更新信息数据")
    @SentinelResource("web-info-update-controller")
    @Operation(description = "修改单条更新信息数据")
    @PutMapping("/update/single")
    public Result<?> updateSingleUpdateInfo(@RequestBody UpdateInfo updateInfo) {

        boolean isUpdated = updateInfoService.updateById(updateInfo);
        return isUpdated ? Result.success(null) : Result.failure(null);

    }


    @GlobalTransactional(name = "delete-single-updateInfo", rollbackFor = Exception.class)
    @LogRecord(description = "删除单条更新信息数据")
    @SentinelResource("web-info-update-controller")
    @Operation(description = "删除单条更新信息数据")
    @DeleteMapping("/delete/single/{id}")
    public Result<?> deleteSingleUpdateInfo(@PathVariable("id") Long id) {

        boolean isDeleted = updateInfoService.removeById(id);
        return isDeleted ? Result.success(null) : Result.failure(null);

    }


}