package ptp.fltv.web.service.mq.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import pfp.fltv.common.exceptions.PtpException;
import pfp.fltv.common.model.po.finance.Commodity;
import pfp.fltv.common.model.po.finance.TransactionRecord;
import pfp.fltv.common.model.po.response.Result;
import ptp.fltv.web.service.mq.service.CommodityService;
import ptp.fltv.web.service.mq.service.TransactionRecordService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/1 PM 11:22:24
 * @description 进行商品秒杀的消费者
 * @filename CommoditySeckillRecordAddConsumer.java
 */

@RequiredArgsConstructor
@RocketMQMessageListener(topic = "commodity-seckill-topic", consumerGroup = "commodity-seckill-consumer-group", consumeThreadNumber = 1, consumeThreadMax = 1, maxReconsumeTimes = 64, delayLevelWhenNextConsume = 2)
@Slf4j
@Service
public class CommoditySeckillConsumer implements RocketMQListener<HashMap<String, Object>> {


    // 2024-10-8  19:59-本机的真实物理IP
    @Value("${ip.physical.self-host:127.0.0.1}")
    private String SELF_HOST_IP;
    private final String ES_UPDATE_COMMODITY_URL = "http://" + SELF_HOST_IP + ":8120/api/v1/service/es/finance/commodity/update/single";


    private final TransactionRecordService transactionRecordService;
    private final RestTemplate restTemplate;
    private final CommodityService commodityService;
    private final StringRedisTemplate stringRedisTemplate;


    @Transactional
    @Override
    public void onMessage(HashMap<String, Object> msg) {

        Integer userId = (Integer) msg.getOrDefault("user-id", -1);
        Integer commodityId = (Integer) msg.getOrDefault("commodity-id", -1);
        Integer count = (Integer) msg.getOrDefault("count", 0);
        Integer replenishmentQuantity = (Integer) msg.getOrDefault("replenishment-quantity", 0);
        String userIp = (String) msg.getOrDefault("user-ip", "未知IP信息");
        String userAgent = (String) msg.getOrDefault("userAgent", "未知代理信息");
        String orderType = (String) msg.getOrDefault("order-type", "normal-order");
        String remark = (String) msg.getOrDefault("remark", "no remark here .");

        // 2024-6-13  23:14-由于商品补货key会被秒杀消费者和秒杀用户同时争抢，存在并发性数据不一致的风险，根本上来说，是 get -> operate ->reset 操作不是原子性导致的，因此我们才决定采用 getAndDelete -> operate ->reset 方案解决可能会出现的 幻觉补货 的诡异情况
        //// 2024-6-11  21:29-这里必须先补货再去秒杀，否则会出现商品抢购一空之后无法补货的类死锁问题
        // String replenishmentQuantityStr = stringRedisTemplate.opsForValue().getAndDelete(String.format("replenish:commodity:%d", commodityId));
        // int newAddStockQuantity = -1;


       /* if (replenishmentQuantityStr != null) {

            String newAddStockQuantityStr = replenishmentQuantityStr.substring(0, replenishmentQuantityStr.length() / 2); // 2024-6-12  00:02-这里一定是整除2的，因为我们是对半放的
            newAddStockQuantity = Integer.parseInt(newAddStockQuantityStr, 2);

        }*/

        // 2024-6-11  21:35-由于我们在CommodityService::seckillOne方法中首先对库存数量进行了判空并且后面又调用了decreaseStockQuantityByIdAndVersion方法，
        // 意义很明确，根本不给我们update方式更新库存的机会，因此我们不能以 CommodityService::seckillOne(commodityId, count - replenishmentQuantity) 合并两个SQL请求为一个了
        // 于是我们只能分别发送两个补货和扣减请求了
        // 但是就在这个时候，您猜怎么着？我们发现 CommodityService::replenishOne这个函数不仅没有对商品库存进行判空检查，而且最终调用的是update方法！(还有，库存更新是进行增量更新的，而不是直接赋值！)
        // 于是乎，我们又可以合二为一啦！

        Commodity modifiedCommodity;
        if ("replenishment-order".equalsIgnoreCase(orderType)) {

            // 2024-6-13  23:19-这里可能会短时间内出现补货失败且库存数量为负导致后续正常秒杀消息失败的情况，由RocketMQ的重试机制兜底，
            // 随着订单逐渐地被处理完毕，争抢不再激烈，补货成功率也就大大提高了，进而商品数量又恢复到正常补货后的水平，后续秒杀订单又可以被正常受理了
            modifiedCommodity = commodityService.replenishOne(commodityId.longValue(), replenishmentQuantity - count);

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
            meta.put("order-type", orderType);

            TransactionRecord record = new TransactionRecord()
                    .setUid(userId.longValue())
                    .setCommodityId(modifiedCommodity.getId())
                    .setCount(count)
                    .setTotalPrice(count * modifiedCommodity.getPrice())
                    .setPaymentMode("Wechat")
                    .setTags(modifiedCommodity.getTags())
                    .setCategory(List.of("commodity", "seckill"))
                    .setMeta(meta).setRemark(remark);

            boolean isSaveRecordSuccessfully = transactionRecordService.save(record);
            mysqlResult.put("is_saved_record", isSaveRecordSuccessfully);

            restTemplate.put(ES_UPDATE_COMMODITY_URL, modifiedCommodity);
            resMap.put("es_result", Result.BLANK);

            if (!isSaveRecordSuccessfully) {

                throw new PtpException(807, "商品交易记录添加失败");

            }

            /*if (replenishmentQuantityStr != null && newAddStockQuantity > 0) {

                stringRedisTemplate.opsForValue().set(String.format("replenish:commodity:%d", commodityId), StringUtils.padToBytes("", '0', 32, StringUtils.Direction.LEFT) + replenishmentQuantityStr.substring(32));

            }*/

            log.info("Commodity seckill message sent successfully ! TransactionRecord = {}", record.toString());
            log.info("result = {}", resMap);

        } else {


            // 2024-6-13  22:56-在补货数量不为0且发生了操作失败，那么一定是补货失败了，因此需要重新将商品补货数量的数据还原回去，因为我们先前是getAndDelete方式获取的库存数据
            /*if (newAddStockQuantity > 0) {

                stringRedisTemplate.opsForValue().set(String.format("replenish:commodity:%d", commodityId), replenishmentQuantityStr);

            }*/

            // 2024-5-24  21:54-MySQL数据库部分执行失败则立即抛异常
            throw new PtpException(807, "商品秒杀失败");

        }


    }


}