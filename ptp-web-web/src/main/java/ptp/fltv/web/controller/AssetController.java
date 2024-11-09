package ptp.fltv.web.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pfp.fltv.common.annotation.LogRecord;
import pfp.fltv.common.model.po.manage.Asset;
import pfp.fltv.common.model.po.manage.User;
import pfp.fltv.common.model.po.response.Result;
import pfp.fltv.common.model.po.system.EventRecord;
import ptp.fltv.web.service.AssetService;
import ptp.fltv.web.service.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 6:58:42
 * @description 财产控制器
 * @filename AssetController.java
 */

@AllArgsConstructor
@Tag(name = "财产操作接口")
@RestController
@RequestMapping("/content/user/asset")
public class AssetController {


    private AssetService assetService;
    private UserService userService;


    @LogRecord(description = "根据财产ID查询财产信息")
    @SentinelResource("web-content-asset-controller")
    @Operation(description = "根据财产ID查询财产信息")
    @GetMapping("/query/single/{userId}")
    public Result<Asset> queryAssetByUserId(

            @Parameter(name = "userId", description = "待查询的财产ID", in = ParameterIn.PATH, required = true) @PathVariable("userId") Long userId

    ) {

        User user = userService.getById(userId);
        Asset asset = assetService.getById(user.getAssetId());

        return (asset == null) ? Result.failure(null) : Result.success(asset);

    }


    @LogRecord(description = "批量(分页)查询多条财产数据")
    @SentinelResource("web-content-asset-controller")
    @Operation(description = "批量(分页)查询多条财产数据")
    @GetMapping("/query/page/{offset}/{limit}")
    public Result<List<Asset>> queryAssetPage(

            @Parameter(name = "offset", description = "查询的一页财产数据的起始偏移量", in = ParameterIn.PATH, required = true) @PathVariable("offset") Long offset,
            @Parameter(name = "limit", description = "查询的这一页财产数据的数量", in = ParameterIn.PATH, required = true) @PathVariable("limit") Long limit

    ) {

        Page<Asset> assetPage = new Page<>(offset, limit);
        assetPage = assetService.page(assetPage);

        return Result.success(assetPage.getRecords() == null ? new ArrayList<>() : assetPage.getRecords());

    }


    @LogRecord(description = "添加财产信息")
    @SentinelResource("web-content-asset-controller")
    @Operation(description = "添加财产信息")
    @PostMapping("/insert/single")
    public Result<Long> insertAsset(

            @Parameter(name = "asset", description = "待添加的财产信息", required = true) @RequestBody Asset asset

    ) {

        boolean isSaved = assetService.save(asset);
        return isSaved ? Result.success(asset.getId()) : Result.failure(-1L);

    }


    @LogRecord(description = "修改财产信息")
    @SentinelResource("web-content-asset-controller")
    @Operation(description = "修改财产信息")
    @PutMapping("/update/single")
    public Result<?> updateAsset(

            @Parameter(name = "asset", description = "待修改的财产信息", required = true) @RequestBody Asset asset

    ) {

        boolean isUpdated = assetService.updateById(asset);
        return isUpdated ? Result.success(null) : Result.failure(null);

    }


    @LogRecord(description = "修改单个财产的余额信息")
    @SentinelResource("web-content-asset-controller")
    @Operation(description = "修改单个财产的余额信息")
    @PutMapping("/update/single/balance")
    public Result<?> changeSingleAssetBalance(

            @Parameter(name = "assetId", description = "待修改的财产的ID", required = true) @RequestParam("assetId") Long assetId,
            @Parameter(name = "userId", description = "动作发起者的用户的ID", required = true) @RequestParam("userId") Long userId,
            @Parameter(name = "eventType", description = "财产余额的变动类型", required = true) @RequestParam("eventType") EventRecord.EventType eventType,
            @Parameter(name = "quantity", description = "财产余额的变动类型", required = true) @RequestParam("quantity") Double quantity,
            @Parameter(name = "remark", description = "财产余额的变动的简短备注", required = true) @RequestParam("remark") String remark

    ) {

        boolean isUpdated = assetService.changeSingleAssetBalance(assetId, userId, eventType, quantity, remark);
        return isUpdated ? Result.success(null) : Result.failure(null);

    }


    @LogRecord(description = "删除财产信息")
    @SentinelResource("web-content-asset-controller")
    @Operation(description = "删除财产信息")
    @DeleteMapping("/delete/single/{id}")
    public Result<?> deleteAsset(

            @Parameter(name = "id", description = "待删除的财产信息ID", in = ParameterIn.PATH, required = true) @PathVariable("id") Long id

    ) {

        boolean isDeleted = assetService.removeById(id);
        return isDeleted ? Result.success(null) : Result.failure(null);

    }


}
