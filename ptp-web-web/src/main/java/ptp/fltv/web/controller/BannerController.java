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
import pfp.fltv.common.model.po.content.Banner;
import pfp.fltv.common.model.po.response.Result;
import ptp.fltv.web.service.BannerService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/10/20 AM 1:20:33
 * @description 轮播控制器
 * @filename BannerController.java
 */

@AllArgsConstructor
@Tag(name = "轮播操作接口")
@RestController
@RequestMapping("/content/banner")
public class BannerController {


    private BannerService bannerService;


    @LogRecord(description = "根据ID查询单条轮播数据")
    @SentinelResource("web-content-banner-controller")
    @Operation(description = "根据ID查询单条轮播数据")
    @GetMapping("/query/single/{id}")
    public Result<Banner> querySingleBanner(

            @Parameter(name = "id", description = "待查询的单条轮播数据ID", in = ParameterIn.PATH, required = true) @PathVariable("id") Long id

    ) {

        Banner banner = bannerService.getById(id);
        return (banner == null) ? Result.failure(null) : Result.success(banner);

    }


    @LogRecord(description = "批量(分页)查询多条轮播数据(对实体可见状态不做限制)")
    @SentinelResource("web-content-banner-controller")
    @Operation(description = "批量(分页)查询多条轮播数据(对实体可见状态不做限制)")
    @GetMapping("/query/page/{pageNum}/{pageSize}")
    public Result<List<Banner>> queryBannerPage(

            @Parameter(name = "pageNum", description = "查询的一页轮播数据的数据页页码", in = ParameterIn.PATH, required = true) @PathVariable("pageNum") Long pageNum,
            @Parameter(name = "pageSize", description = "查询的这一页轮播数据的数量", in = ParameterIn.PATH, required = true) @PathVariable("pageSize") Long pageSize

    ) {

        Page<Banner> bannerPage = new Page<>(pageNum, pageSize);
        bannerPage = bannerService.page(bannerPage);

        return Result.success(bannerPage.getRecords() == null ? new ArrayList<>() : bannerPage.getRecords());

    }


    @LogRecord(description = "查询全部可见的轮播数据")
    @SentinelResource("web-content-banner-controller")
    @Operation(description = "查询全部可见的轮播数据")
    @GetMapping("/query/all/available")
    public Result<List<Banner>> queryAllBanners() {

        QueryWrapper<Banner> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", ContentStatus.NORMAL.getCode());
        List<Banner> banners = bannerService.list(queryWrapper);
        return Result.success(banners);

    }


    @GlobalTransactional(name = "insert-single-banner", rollbackFor = Exception.class)
    @LogRecord(description = "添加单条轮播数据")
    @SentinelResource("web-content-banner-controller")
    @Operation(description = "添加单条轮播数据")
    @PostMapping("/insert/single")
    public Result<Long> insertSingleBanner(

            @Parameter(name = "banner", description = "待添加的单条轮播数据", required = true) @RequestBody Banner banner

    ) {

        boolean isSaved = bannerService.save(banner);
        return isSaved ? Result.success(banner.getId()) : Result.failure(-1L);

    }


    @GlobalTransactional(name = "update-single-banner", rollbackFor = Exception.class)
    @LogRecord(description = "修改单条轮播数据")
    @SentinelResource("web-content-banner-controller")
    @Operation(description = "修改单条轮播数据")
    @PutMapping("/update/single")
    public Result<?> updateSingleBanner(@RequestBody Banner banner) {

        boolean isUpdated = bannerService.updateById(banner);
        return isUpdated ? Result.success(null) : Result.failure(null);

    }


    @GlobalTransactional(name = "delete-single-banner", rollbackFor = Exception.class)
    @LogRecord(description = "删除单条轮播数据")
    @SentinelResource("web-content-banner-controller")
    @Operation(description = "删除单条轮播数据")
    @DeleteMapping("/delete/single/{id}")
    public Result<?> deleteSingleBanner(@PathVariable("id") Long id) {

        boolean isDeleted = bannerService.removeById(id);
        return isDeleted ? Result.success(null) : Result.failure(null);

    }


}
