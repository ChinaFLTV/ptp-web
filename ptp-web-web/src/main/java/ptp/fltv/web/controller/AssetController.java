package ptp.fltv.web.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import pfp.fltv.common.annotation.LogRecord;
import pfp.fltv.common.model.po.manage.Asset;
import pfp.fltv.common.model.po.manage.User;
import pfp.fltv.common.model.vo.AssetVo;
import pfp.fltv.common.response.Result;
import ptp.fltv.web.service.AssetService;
import ptp.fltv.web.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @GetMapping("/query/{userId}")
    public Result<Asset> queryAssetByUserId(
            @Parameter(name = "userId", description = "待查询的财产ID", in = ParameterIn.PATH, required = true)
            @PathVariable("userId")
            Long userId) {

        User user = userService.getById(userId);
        Asset asset = assetService.getById(user.getAssetId());

        return (asset == null) ? Result.failure(null) : Result.success(asset);

    }


    @LogRecord(description = "批量(分页)查询多条财产数据")
    @SentinelResource("web-content-asset-controller")
    @Operation(description = "批量(分页)查询多条财产数据")
    @GetMapping("/query/page/{offset}/{limit}")
    public Result<List<AssetVo>> queryPassagePage(

            @Parameter(name = "offset", description = "查询的一页财产数据的起始偏移量", in = ParameterIn.PATH, required = true) @PathVariable("offset") Long offset,
            @Parameter(name = "limit", description = "查询的这一页财产数据的数量", in = ParameterIn.PATH, required = true) @PathVariable("limit") Long limit

    ) {

        Page<Asset> assetPage = new Page<>(offset, limit);
        assetPage = assetService.page(assetPage);

        List<AssetVo> assetVos = new ArrayList<>();
        for (Asset asset : assetPage.getRecords()) {

            AssetVo assetVo = new AssetVo();
            BeanUtils.copyProperties(asset, assetVo);
            assetVos.add(assetVo);

        }

        return Result.success(assetVos);

    }


    @LogRecord(description = "添加财产信息")
    @SentinelResource("web-content-asset-controller")
    @Operation(description = "添加财产信息")
    @PostMapping("/insert")
    public Result<?> insertAsset(
            @Parameter(name = "asset", description = "待添加的财产信息", required = true)
            @RequestBody
            Asset asset) {

        boolean isSaved = assetService.save(asset);

        Map<String, Object> map = new HashMap<>();
        map.put("isSaved", isSaved);
        return isSaved ? Result.success(map) : Result.failure(map);

    }


    @LogRecord(description = "修改财产信息")
    @SentinelResource("web-content-asset-controller")
    @Operation(description = "修改财产信息")
    @PutMapping("/update")
    public Result<?> updateAsset(
            @Parameter(name = "asset", description = "待修改的财产信息", required = true)
            @RequestBody
            Asset asset) {

        boolean isUpdated = assetService.updateById(asset);

        Map<String, Object> map = new HashMap<>();
        map.put("isUpdated", isUpdated);
        return isUpdated ? Result.success(map) : Result.failure(map);

    }


    @LogRecord(description = "删除财产信息")
    @SentinelResource("web-content-asset-controller")
    @Operation(description = "删除财产信息")
    @DeleteMapping("/delete/{id}")
    public Result<?> deleteAsset(
            @Parameter(name = "id", description = "待删除的财产信息ID", in = ParameterIn.PATH, required = true)
            @PathVariable("id")
            Long id) {

        boolean isDeleted = assetService.removeById(id);

        Map<String, Object> map = new HashMap<>();
        map.put("isDeleted", isDeleted);
        return isDeleted ? Result.success(map) : Result.failure(map);

    }


}
