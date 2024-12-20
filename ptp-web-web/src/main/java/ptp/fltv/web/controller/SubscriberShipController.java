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
import pfp.fltv.common.model.po.manage.SubscriberShip;
import pfp.fltv.common.model.po.response.Result;
import ptp.fltv.web.service.SubscriberShipService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/10/21 PM 7:20:32
 * @description 订阅关系控制器
 * @filename SubscriberShipController.java
 */

@AllArgsConstructor
@Tag(name = "订阅关系操作接口")
@RestController
@RequestMapping("/user/subscriberShip")
public class SubscriberShipController {


    private SubscriberShipService subscriberShipService;


    @LogRecord(description = "根据ID查询单条订阅关系数据")
    @SentinelResource("web-user-subscriberShip-controller")
    @Operation(description = "根据ID查询单条订阅关系数据")
    @GetMapping("/query/single/{id}")
    public Result<SubscriberShip> querySingleSubscriberShip(

            @Parameter(name = "id", description = "待查询的单条订阅关系ID", in = ParameterIn.PATH, required = true) @PathVariable("id") Long id

    ) {

        SubscriberShip subscriberShip = subscriberShipService.getById(id);
        return (subscriberShip == null) ? Result.failure(null) : Result.success(subscriberShip);

    }


    @LogRecord(description = "根据订阅操作的双方ID查询单条订阅关系数据")
    @SentinelResource("web-content-user-controller")
    @Operation(description = "根据订阅操作的双方ID查询单条订阅关系数据")
    @GetMapping("/query/single/subscription")
    public Result<SubscriberShip> querySingleSubscription(

            @Parameter(name = "followerId", description = "订阅发起方的用户ID", required = true) @RequestParam("followerId") Long followerId,
            @Parameter(name = "followeeId", description = "被订阅方的用户ID", required = true) @RequestParam("followeeId") Long followeeId

    ) {

        SubscriberShip subscriberShip = subscriberShipService.querySingleSubscription(followerId, followeeId);
        return (subscriberShip == null) ? Result.failure(null) : Result.success(subscriberShip);

    }


    @LogRecord(description = "批量(分页)查询多条订阅关系数据")
    @SentinelResource("web-user-subscriberShip-controller")
    @Operation(description = "批量(分页)查询多条订阅关系数据")
    @GetMapping("/query/page/{offset}/{limit}")
    public Result<List<SubscriberShip>> querySubscriberShipPage(

            @Parameter(name = "offset", description = "查询的一页订阅关系数据的起始偏移量", in = ParameterIn.PATH, required = true) @PathVariable("offset") Long offset,
            @Parameter(name = "limit", description = "查询的这一页订阅关系数据的数量", in = ParameterIn.PATH, required = true) @PathVariable("limit") Long limit

    ) {

        Page<SubscriberShip> subscriberShipPage = new Page<>(offset, limit);
        subscriberShipPage = subscriberShipService.page(subscriberShipPage);

        return subscriberShipPage.getRecords() == null ? Result.failure(new ArrayList<>()) : Result.success(subscriberShipPage.getRecords());

    }


    @GlobalTransactional(name = "insert-single-user-subscriberShip", rollbackFor = Exception.class)
    @LogRecord(description = "添加单条订阅关系数据")
    @SentinelResource("web-user-subscriberShip-controller")
    @Operation(description = "添加单条订阅关系数据")
    @PostMapping("/insert/single")
    public Result<Long> insertSingleSubscriberShip(

            @Parameter(name = "subscriberShip", description = "待添加的单条订阅关系数据", required = true) @RequestBody SubscriberShip subscriberShip

    ) {

        boolean isSaved = subscriberShipService.save(subscriberShip);
        return isSaved ? Result.success(subscriberShip.getId()) : Result.failure(-1L);

    }


    @GlobalTransactional(name = "insert-single-user-subscriberShip", rollbackFor = Exception.class)
    @LogRecord(description = "根据订阅操作的双方ID添加单条订阅关系数据")
    @SentinelResource("web-user-subscriberShip-controller")
    @Operation(description = "根据订阅操作的双方ID添加单条订阅关系数据")
    @PostMapping("/insert/single/subscription")
    public Result<?> insertSingleSubscription(

            @Parameter(name = "followerId", description = "订阅发起方的用户ID", required = true) @RequestParam("followerId") Long followerId,
            @Parameter(name = "followeeId", description = "被订阅方的用户ID", required = true) @RequestParam("followeeId") Long followeeId

    ) {

        long savedSubscriptionId = subscriberShipService.insertSingleSubscription(followerId, followeeId);
        return savedSubscriptionId > 0 ? Result.success(savedSubscriptionId) : Result.failure(null);

    }


    @GlobalTransactional(name = "update-single-user-subscriberShip", rollbackFor = Exception.class)
    @LogRecord(description = "修改单条订阅关系数据")
    @SentinelResource("web-user-subscriberShip-controller")
    @Operation(description = "修改单条订阅关系数据")
    @PutMapping("/update/single")
    public Result<?> updateSingleSubscriberShip(

            @Parameter(name = "subscriberShip", description = "待修改的单条订阅关系数据", required = true) @RequestBody SubscriberShip subscriberShip

    ) {

        boolean isUpdated = subscriberShipService.updateById(subscriberShip);
        return isUpdated ? Result.success(null) : Result.failure(null);

    }


    @GlobalTransactional(name = "delete-single-user-subscriberShip", rollbackFor = Exception.class)
    @LogRecord(description = "根据ID删除单条订阅关系数据")
    @SentinelResource("web-user-subscriberShip-controller")
    @Operation(description = "根据ID删除单条订阅关系数据")
    @DeleteMapping("/delete/single/{id}")
    public Result<?> deleteSingleSubscriberShip(

            @Parameter(name = "id", description = "待删除的单条订阅关系ID", in = ParameterIn.PATH, required = true) @PathVariable("id") Long id

    ) {

        boolean isDeleted = subscriberShipService.removeById(id);
        return isDeleted ? Result.success(null) : Result.failure(null);

    }


    @GlobalTransactional(name = "delete-single-user-subscriberShip", rollbackFor = Exception.class)
    @LogRecord(description = "根据解除订阅操作的双方ID删除单条订阅关系数据")
    @SentinelResource("web-user-subscriberShip-controller")
    @Operation(description = "根据解除订阅操作的双方ID删除单条订阅关系数据")
    @DeleteMapping("/delete/single/subscription")
    public Result<?> deleteSingleSubscription(

            @Parameter(name = "followerId", description = "解除订阅发起方的用户ID", required = true) @RequestParam("followerId") Long followerId,
            @Parameter(name = "followeeId", description = "被解除订阅方的用户ID", required = true) @RequestParam("followeeId") Long followeeId

    ) {

        boolean isDeleted = subscriberShipService.deleteSingleSubscription(followerId, followeeId);
        return isDeleted ? Result.success(null) : Result.failure(null);

    }


    @LogRecord(description = "根据订阅发起方ID查询全部发起的订阅关系的总数")
    @SentinelResource("web-user-subscriberShip-controller")
    @Operation(description = "根据订阅发起方ID查询全部发起的订阅关系的总数")
    @GetMapping("/countAllSubscriptionsByFollowerId")
    public Result<Long> countAllSubscriptionsByFollowerId(

            @Parameter(name = "followerId", description = "订阅发起方的用户ID", required = true) @RequestParam("followerId") Long followerId

    ) {

        Long count = subscriberShipService.countAllSubscriptionsByFollowerId(followerId);
        return count >= 0 ? Result.success(count) : Result.failure(-1L);

    }


    @LogRecord(description = "根据被订阅方ID查询全部被发起的订阅关系的总数")
    @SentinelResource("web-user-subscriberShip-controller")
    @Operation(description = "根据被订阅方ID查询全部被发起的订阅关系的总数")
    @GetMapping("/countAllSubscriptionsByFolloweeId")
    public Result<Long> countAllSubscriptionsByFolloweeId(

            @Parameter(name = "followeeId", description = "被订阅方的用户ID", required = true) @RequestParam("followeeId") Long followeeId

    ) {

        Long count = subscriberShipService.countAllSubscriptionsByFolloweeId(followeeId);
        return count >= 0 ? Result.success(count) : Result.failure(-1L);

    }


}
