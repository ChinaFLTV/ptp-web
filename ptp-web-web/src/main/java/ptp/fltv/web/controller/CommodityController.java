package ptp.fltv.web.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pfp.fltv.common.exceptions.PtpException;
import pfp.fltv.common.model.po.finance.Commodity;
import pfp.fltv.common.model.po.finance.TransactionRecord;
import pfp.fltv.common.response.Result;
import ptp.fltv.web.constants.WebConstants;
import ptp.fltv.web.mq.CommodityMqService;
import ptp.fltv.web.service.CommodityService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/23 PM 10:04:53
 * @description 商品控制器
 * @filename CommodityController.java
 */

@AllArgsConstructor
@Tag(name = "商品操作接口")
@RestController
@RequestMapping("/finance/commodity")
public class CommodityController {


    private static final String ES_PREFIX_COMMODITY_URL = WebConstants.ES_BASE_URL + WebConstants.ES_CONTEXT_URL + WebConstants.ES_BASE_COMMODITY_URL;
    private static final String ES_INSERT_COMMODITY_URL = ES_PREFIX_COMMODITY_URL + "/insert/single";
    private static final String ES_UPDATE_COMMODITY_URL = ES_PREFIX_COMMODITY_URL + "/update/single";
    private static final String ES_DELETE_COMMODITY_URL = ES_PREFIX_COMMODITY_URL + "/delete/single/{id}";


    private CommodityService commodityService;
    private RestTemplate restTemplate;
    private RocketMQTemplate rocketMQTemplate;
    private CommodityMqService commodityMqService;
    private RedissonClient redissonClient;


    @SentinelResource("web-finance-commodity-controller")
    @Operation(description = "根据ID查询单个商品数据")
    @GetMapping("/query/single/{id}")
    public Result<Commodity> querySingleCommodity(

            @Parameter(name = "id", description = "待查询的单个商品ID", in = ParameterIn.PATH) @PathVariable("id") Long id

    ) {

        Commodity commodity = commodityService.getOneById(id);

        return (commodity == null) ? Result.failure(null) : Result.success(commodity);

    }


    @SentinelResource("web-finance-commodity-controller")
    @Operation(description = "批量(分页)查询多个商品数据")
    @GetMapping("/query/page/{offset}/{limit}")
    public Result<List<Commodity>> queryCommodityPage(

            @Parameter(name = "offset", description = "查询的一页商品数据的起始偏移量", in = ParameterIn.PATH) @PathVariable("offset") Long offset, @Parameter(name = "limit", description = "查询的这一页商品数据的数量", in = ParameterIn.PATH) @PathVariable("limit") Long limit

    ) {

        return Result.success(commodityService.getListByRange(offset, limit, true));

    }


    @SentinelResource("web-finance-commodity-controller")
    @Operation(description = "添加单个商品数据")
    @PostMapping("/insert/single")
    public Result<?> insertSingleAnnouncement(

            @Parameter(name = "commodity", description = "待添加的单个商品数据") @RequestBody Commodity commodity

    ) {

        boolean isSaved = commodityService.insertOne(commodity) > 0;

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mysqlResult = new HashMap<>();
        mysqlResult.put("isSaved", isSaved);
        map.put("mysql_result", mysqlResult);

        if (isSaved) {

            Result<?> result = restTemplate.postForObject(ES_INSERT_COMMODITY_URL, commodity, Result.class);
            map.put("es_result", result);

        }

        return Result.neutral(map);

    }


    @SentinelResource("web-finance-commodity-controller")
    @Operation(description = "修改单个商品数据")
    @PutMapping("/update/single")
    public Result<?> updateSingleCommodity(

            @Parameter(name = "commodity", description = "待修改的单个商品数据") @RequestBody Commodity commodity

    ) {

        boolean isUpdated = commodityService.updateOne(commodity) > 0;

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mysqlResult = new HashMap<>();
        mysqlResult.put("isUpdated", isUpdated);
        map.put("mysql_result", mysqlResult);

        if (isUpdated) {

            restTemplate.put(ES_UPDATE_COMMODITY_URL, commodity);
            map.put("es_result", Result.BLANK);

        }

        return Result.neutral(map);

    }


    @SentinelResource("web-finance-commodity-controller")
    @Operation(description = "删除单个商品数据")
    @DeleteMapping("/delete/single/{id}")
    public Result<?> deleteSingleCommodity(@Parameter(name = "id", description = "待删除的单个公告ID", in = ParameterIn.PATH) @PathVariable("id") Long id) {

        boolean isDeleted = commodityService.deleteOne(id) > 0;

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mysqlResult = new HashMap<>();
        mysqlResult.put("isDeleted", isDeleted);
        map.put("mysql_result", mysqlResult);

        if (isDeleted) {

            Map<String, Object> urlValues = new HashMap<>();
            urlValues.put("id", id);
            restTemplate.delete(ES_DELETE_COMMODITY_URL, urlValues);
            map.put("es_result", Result.BLANK);

        } else {

            throw new PtpException(807);

        }

        return Result.neutral(map);

    }


    @Transactional
    @SentinelResource("web-finance-commodity-controller")
    @Operation(description = "根据ID秒杀一个商品")
    @PutMapping("/extension/seckill")
    public Result<?> seckillSingleCommodity(

            @Parameter(name = "id", description = "待秒杀的单个商品ID") @RequestParam("id") Long id, @Parameter(name = "count", description = "待秒杀的单个商品的数量") @RequestParam("count") Integer count, HttpServletRequest request

    ) throws InterruptedException {

        long uid = Long.parseLong(request.getHeader("uid") == null ? "-1" : request.getHeader("uid"));
        if (uid < 0) {

            throw new PtpException(809, "请求头部参数缺少UID");

        }

        // 2024-6-6  23:18-使用Redis的分布式锁来完成排队秒杀
        RLock lock = redissonClient.getLock(String.format("lock:commodity:seckill:%d", id));
        boolean isLocked = lock.tryLock(500, TimeUnit.MILLISECONDS);// 2024-6-6  23:19-设置的超时时间越长，最大延时也就越高，吞吐量也就越低，但超卖的概率会变低；反之亦然

        // 2024-6-6  23:22-未能在规定时间内获取到锁，则以异常抛出结束本次业务操作
        if (!isLocked) {

            throw new PtpException(810, "商品秒杀失败");

        }

        try {

            Commodity modifiedCommodity = commodityService.seckillOne(id, count);

            Map<String, Object> map = new HashMap<>();
            Map<String, Object> mysqlResult = new HashMap<>();
            mysqlResult.put("isUpdated", modifiedCommodity != null);
            map.put("mysql_result", mysqlResult);

            if (modifiedCommodity != null) {

                restTemplate.put(ES_UPDATE_COMMODITY_URL, modifiedCommodity);
                map.put("es_result", Result.BLANK);

                TransactionRecord record = new TransactionRecord().setUid(uid).setCommodityId(modifiedCommodity.getId()).setCount(count).setTotalPrice(count * modifiedCommodity.getPrice()).setPaymentMode("Wechat").setTags(modifiedCommodity.getTags()).setCategory(List.of("commodity", "seckill")).setCreateTime(Timestamp.from(Instant.now()));

                commodityMqService.asyncSendOrderAddMsg("commodity-seckill-record-add-topic", record, null, null);

            } else {

                // 2024-5-24  21:54-MySQL数据库部分执行失败则立即抛异常
                throw new PtpException(807, "商品秒杀失败");

            }

            return Result.neutral(map);

        } finally {

            lock.unlock();

        }

    }


    @Transactional
    @SentinelResource("web-finance-commodity-controller")
    @Operation(description = "根据ID给一种商品补货")
    @PutMapping("/extension/replenish")
    public Result<?> replenishSingleCommodity(

            @Parameter(name = "id", description = "待补货的单个商品ID") @RequestParam("id") Long id, @Parameter(name = "count", description = "待补货的单个商品的数量") @RequestParam("count") Integer count

    ) {

        Commodity modifiedCommodity = commodityService.replenishOne(id, count);

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mysqlResult = new HashMap<>();
        mysqlResult.put("isUpdated", modifiedCommodity != null);
        map.put("mysql_result", mysqlResult);

        if (modifiedCommodity != null) {

            restTemplate.put(ES_UPDATE_COMMODITY_URL, modifiedCommodity);
            map.put("es_result", Result.BLANK);

        }

        return Result.neutral(map);

    }


}