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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pfp.fltv.common.exceptions.PtpException;
import pfp.fltv.common.model.po.finance.Commodity;
import pfp.fltv.common.response.Result;
import pfp.fltv.common.utils.StringUtils;
import ptp.fltv.web.annotation.CheckCostTime;
import ptp.fltv.web.constants.WebConstants;
import ptp.fltv.web.mq.CommodityMqService;
import ptp.fltv.web.service.CommodityService;

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
    private StringRedisTemplate stringRedisTemplate;


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


    // @CheckCostTime
    // @Transactional
    @SentinelResource("web-finance-commodity-controller")
    @Operation(description = "根据ID秒杀一个商品")
    @PutMapping("/extension/seckill")
    public Result<?> seckillSingleCommodity(

            @Parameter(name = "id", description = "待秒杀的单个商品ID") @RequestParam("id") Long id, @Parameter(name = "count", description = "待秒杀的单个商品的数量") @RequestParam("count") Integer count,
            HttpServletRequest request

    ) throws InterruptedException {

        // 2024-6-7  23:26-在获取锁之前提前校验显式信息，以避免获取到锁之后信息缺少无效的尴尬情况
        long uid = Long.parseLong(request.getHeader("uid") == null ? "-1" : request.getHeader("uid"));
        if (uid < 0) {

            throw new PtpException(809, "请求头部参数缺少UID");

        }
        if (id < 0 || count <= 0) {

            throw new PtpException(812, "商品ID或商品秒杀数量无效");

        }

        // 2024-6-6  23:18-使用Redis的分布式锁来完成排队秒杀
        RLock lock = redissonClient.getLock(String.format("lock:commodity:seckill:%d", id));

        try {

            boolean isLocked = lock.tryLock(500L, TimeUnit.MILLISECONDS);// 2024-6-6  23:19-设置的超时时间越长，最大延时也就越高，吞吐量也就越低，但超卖的概率会变低；反之亦然

            // 2024-6-6  23:22-未能在规定时间内获取到锁，则以异常抛出结束本次业务操作
            if (!isLocked) {

                throw new PtpException(810, "商品秒杀失败", "获取锁超时");

            }

            // 2024-6-7  23:13-这里之所以不检查而直接进行库存扣减操作，是因为这样做能在一定程度上降低Redis指令的评价发送数量
            // 正常情况：先获取当前库存数量的值(1次)，然后再执行扣减操作(2次)
            // 成功(2次)、失败(1次)
            // 现在情况：直接扣减(1次)再判断是否透支了库存数量
            // 成功(1次)、失败(1次)
            Long newStockQuantity = stringRedisTemplate.opsForValue().decrement(String.format("stock-quantity:commodity:seckill:%d", id), count);
            if (newStockQuantity == null) {

                throw new PtpException(811, "商品秒杀异常", "获取到扣减商品后的商品库存数量为null");

            }

            // 2024-6-11  23:18-这里由于Redis和MySQL、ElasticSearch这两处的库存数量更新不是同步的，所以可能会产生一致性问题，但是，若真如此吗？我们来分析一下(以秒杀100件商品，中途补货50件为例)
            // 如果 Redis 更新早于 MySQL&ElasticSearch：
            //     那么由于是秒杀场景，可能会瞬间产生了150个成功的秒杀请求(100(原) + 50(补))
            //     MySQL这边尽管还没更新，但由于我们是将秒杀请求异步放进消息队列中，然后异步串行消费这些请求，
            //     而且我们不再使用会对库存进行预先的判空判断的seckillOne方法，而是转而使用既能将MySQL请求合二为一还能总是更新成功的replenishOne方法
            //     它会在将最新的补货数量连同秒杀数量进行合成，更新至MySQL&ElasticSearch中，所以就即使更新慢到不行，我们也同样会生成抢锁成功的数量相同的订单
            //     因此，这种情况下，不会有一致性问题，与 超前消费 差不多，但最终我们的库存是会成功补上的
            // 如果 Redis 更新晚于 MySQL&ElasticSearch：
            //     那么MySQL和ElasticSearch这边库存显得非常充足，而Redis这边的库存则显得有点尴尬
            //     此时，秒杀请求将仍旧按照旧库存进行封控，直至Redis成功更新完库存之后，才去按照新库存进行封控
            //     所以，很显然，这种情况下也同样不会存在一致性问题，但可能会存在一定的补货时延
            // 如果 Redis 与 MySQL&ElasticSearch 同时更新：
            //     这是理想情况，当然不会存在一致性问题哈
            // 2024-6-11  22:04-同样地，这里也需要先补货再秒杀，否则同样会出现因库存为空且无法补货的尴尬情形
            String replenishmentQuantityStr = stringRedisTemplate.opsForValue().get(String.format("replenish:commodity:%d", id));
            String newAddStockQuantityStr = replenishmentQuantityStr.substring(32);
            int newAddStockQuantity = Integer.parseInt(newAddStockQuantityStr, 2);

            // 2024-6-11  23:52-这里我们规定：凡是用户秒杀时机与补货时机相同但此时处于售罄状态时，秒杀操作一律失败
            if (newStockQuantity + count <= 0) {

                stringRedisTemplate.opsForValue().set(String.format("stock-quantity:commodity:seckill:%d", id), String.valueOf(newAddStockQuantity));
                // 2024-6-12  00:07-这里不能直接以取到的自己的低32位作为新的高32位处理，因为你不知道在你消费这低32位之前，高32为是否已经被消费掉了
                stringRedisTemplate.opsForValue().set(String.format("replenish:commodity:%d", id), replenishmentQuantityStr.substring(0, replenishmentQuantityStr.length() / 2) + StringUtils.padToBytes("", '0', 32));

                throw new PtpException(811, "商品已售罄", "扣减商品后的商品库存数量为负");

            } else {

                stringRedisTemplate.opsForValue().set(String.format("stock-quantity:commodity:seckill:%d", id), String.valueOf(newStockQuantity + newAddStockQuantity));
                stringRedisTemplate.opsForValue().set(String.format("replenish:commodity:%d", id), replenishmentQuantityStr.substring(0, replenishmentQuantityStr.length() / 2) + StringUtils.padToBytes("", '0', 32));

            }

            // 2024-6-7  23:15-你会发现在这里我们几乎没有进行耗时操作，都是对现有数据的收集与简单处理
            HashMap<String, Object> msg = new HashMap<>();
            msg.put("user-id", uid);
            msg.put("commodity-id", id);
            msg.put("count", count);
            msg.put("user-ip", request.getHeader("X-Forward-For"));
            msg.put("user-agent", request.getHeader(HttpHeaders.USER_AGENT));

            commodityMqService.asyncSendOrderAddMsg("commodity-seckill-topic", msg, null, null);

            Map<String, Object> resMap = new HashMap<>();
            resMap.put("msg", "商品秒杀成功!订单回显可能存在延时，请耐心等待");
            return Result.success(resMap);

        } finally {

            lock.unlock();

        }

    }


    @CheckCostTime
    @Transactional
    @SentinelResource("web-finance-commodity-controller")
    @Operation(description = "根据ID给一种商品补货")
    @PutMapping("/extension/replenish")
    public Result<?> replenishSingleCommodity(

            @Parameter(name = "id", description = "待补货的单个商品ID") @RequestParam("id") Long id, @Parameter(name = "count", description = "待补货的单个商品的数量") @RequestParam("count") Integer count,
            HttpServletRequest request

    ) {

        long uid = Long.parseLong(request.getHeader("uid") == null ? "-1" : request.getHeader("uid"));
        if (uid < 0) {

            throw new PtpException(809, "请求头部参数缺少UID");

        }
        if (id < 0 || count <= 0) {

            throw new PtpException(812, "商品ID或商品秒杀数量无效");

        }

        // 2024-6-10  21:47-尽管商品补货不会出现大量商家抢着给某一种商品补货的迷惑情况，
        // 但由于是在大量用户秒杀过程中进行补货，这个过程可能会出现比较激烈的锁竞争，因此需要以MQ的形式进行异步补货，
        // 补货开始结果响应尽早返回，补货完成结果响应则以消息通知的形式发送给商家
        HashMap<String, Object> msg = new HashMap<>();
        msg.put("user-id", uid);
        msg.put("commodity-id", id);
        msg.put("count", count);
        msg.put("user-ip", request.getHeader("X-Forward-For"));
        msg.put("user-agent", request.getHeader(HttpHeaders.USER_AGENT));
        commodityMqService.asyncSendOrderAddMsg("commodity-replenish-topic", msg, null, null);

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("msg", "商品补货请求已被接收!请耐心等待商品补货完成通知(商品秒杀期间可能会有一定的延迟)");
        return Result.success(resMap);

    }


}