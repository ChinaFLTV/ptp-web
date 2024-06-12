package ptp.fltv.web.service.mq.consumer;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import pfp.fltv.common.exceptions.PtpException;
import pfp.fltv.common.model.po.finance.Commodity;
import pfp.fltv.common.model.po.finance.TransactionRecord;
import pfp.fltv.common.response.Result;
import pfp.fltv.common.utils.StringUtils;
import ptp.fltv.web.service.mq.service.CommodityService;
import ptp.fltv.web.service.mq.service.TransactionRecordService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/1 PM 11:22:24
 * @description 用于进行商品秒杀的消费者
 * @filename CommoditySeckillRecordAddConsumer.java
 */

@AllArgsConstructor
@RocketMQMessageListener(topic = "commodity-seckill-topic", consumerGroup = "commodity-seckill-consumer-group")
@Slf4j
@Service
public class CommoditySeckillConsumer implements RocketMQListener<HashMap<String, Object>> {


    private static final String ES_UPDATE_COMMODITY_URL = "http://127.0.0.1:8120/api/v1/service/es/finance/commodity/update/single";


    private TransactionRecordService transactionRecordService;
    private RestTemplate restTemplate;
    private CommodityService commodityService;
    private StringRedisTemplate stringRedisTemplate;


    @Transactional
    @Override
    public void onMessage(HashMap<String, Object> msg) {

        Integer userId = (Integer) msg.getOrDefault("user-id", -1);
        Integer commodityId = (Integer) msg.getOrDefault("commodity-id", -1);
        Integer count = (Integer) msg.getOrDefault("count", 0);
        String userIp = (String) msg.getOrDefault("user-ip", "未知IP信息");
        String userAgent = (String) msg.getOrDefault("userAgent", "未知代理信息");
        String type = (String) msg.getOrDefault("type", "normal-order");
        String remark = (String) msg.getOrDefault("remark", "no remark here .");


        //// 2024-6-11  21:29-这里必须先补货再去秒杀，否则会出现商品抢购一空之后无法补货的类死锁问题
        String replenishmentQuantityStr = stringRedisTemplate.opsForValue().getAndDelete(String.format("replenish:commodity:%d", commodityId));
        int newAddStockQuantity = -1;
        if (replenishmentQuantityStr != null) {

            String newAddStockQuantityStr = replenishmentQuantityStr.substring(0, replenishmentQuantityStr.length() / 2); // 2024-6-12  00:02-这里一定是整除2的，因为我们是对半放的
            newAddStockQuantity = Integer.parseInt(newAddStockQuantityStr, 2);

        }

        // 2024-6-11  21:35-由于我们在CommodityService::seckillOne方法中首先对库存数量进行了判空并且后面又调用了decreaseStockQuantityByIdAndVersion方法，
        // 意义很明确，根本不给我们update方式更新库存的机会，因此我们不能以 CommodityService::seckillOne(commodityId, count - replenishmentQuantity) 合并两个SQL请求为一个了
        // 于是我们只能分别发送两个补货和扣减请求了
        // 但是就在这个时候，您猜怎么着？我们发现 CommodityService::replenishOne这个函数不仅没有对商品库存进行判空检查，而且最终调用的是update方法！(还有，库存更新是进行增量更新的，而不是直接赋值！)
        // 于是乎，我们又可以合二为一啦！

        Commodity modifiedCommodity;
        if (newAddStockQuantity > 0) {

            modifiedCommodity = commodityService.replenishOne(commodityId.longValue(), newAddStockQuantity - count);

        } else {

            // 2024-6-12  23:20-我说怎么换上这个函数就不会出现数据不一致的情况呢，原来这方法是通过CAS的方式更新的，而上面那个就是普通更新操作，在高并发的情况下肯定会出现数据不一致的问题
            modifiedCommodity = commodityService.seckillOne(commodityId.longValue(), count);

        }
        // Commodity modifiedCommodity = commodityService.seckillOne(commodityId.longValue(), count);

        Map<String, Object> resMap = new HashMap<>();
        Map<String, Object> mysqlResult = new HashMap<>();
        mysqlResult.put("isUpdated", modifiedCommodity != null);
        resMap.put("mysql_result", mysqlResult);

        if (modifiedCommodity != null) {

            Map<String, Object> meta = new HashMap<>();
            meta.put("type", type);

            TransactionRecord record = new TransactionRecord().setUid(userId.longValue())
                    .setCommodityId(modifiedCommodity.getId())
                    .setCount(count)
                    .setTotalPrice(count * modifiedCommodity.getPrice())
                    .setPaymentMode("Wechat")
                    .setTags(modifiedCommodity.getTags())
                    .setCategory(List.of("commodity", "seckill"))
                    .setMeta(JSON.toJSONString(meta))
                    .setCreateTime(Timestamp.from(Instant.now()))
                    .setUpdateTime(Timestamp.from(Instant.now()))
                    .setRemark(remark);

            boolean isSaveRecordSuccessfully = transactionRecordService.save(record);
            mysqlResult.put("is_saved_record", isSaveRecordSuccessfully);

            restTemplate.put(ES_UPDATE_COMMODITY_URL, modifiedCommodity);
            resMap.put("es_result", Result.BLANK);

            if (!isSaveRecordSuccessfully) {

                throw new PtpException(807, "商品交易记录添加失败");

            }

            if (replenishmentQuantityStr != null && newAddStockQuantity > 0) {

                stringRedisTemplate.opsForValue().set(String.format("replenish:commodity:%d", commodityId), StringUtils.padToBytes("", '0', 32, StringUtils.Direction.LEFT) + replenishmentQuantityStr.substring(32));

            }

            log.info("Commodity seckill message sent successfully ! TransactionRecord = {}", record.toString());
            log.info("result = {}", resMap);

        } else {

            // 2024-5-24  21:54-MySQL数据库部分执行失败则立即抛异常
            throw new PtpException(807, "商品秒杀失败");

        }


    }


}