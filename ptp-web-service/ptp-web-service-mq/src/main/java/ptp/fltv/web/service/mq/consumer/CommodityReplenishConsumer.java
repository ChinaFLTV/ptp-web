package ptp.fltv.web.service.mq.consumer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import pfp.fltv.common.exceptions.PtpException;
import ptp.fltv.web.service.mq.service.CommodityService;
import ptp.fltv.web.service.mq.service.TransactionRecordService;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/10 PM 10:01:21
 * @description 用于添加商品补货的消费者
 * @filename CommodityReplenishConsumer.java
 */

@AllArgsConstructor
@RocketMQMessageListener(topic = "commodity-replenish-topic", consumerGroup = "commodity-replenish-consumer-group", consumeThreadNumber = 10, consumeThreadMax = 10, maxReconsumeTimes = 64, delayLevelWhenNextConsume = 2)
@Slf4j
@Service
public class CommodityReplenishConsumer implements RocketMQListener<HashMap<String, Object>> {


    private static final String ES_UPDATE_COMMODITY_URL = "http://127.0.0.1:8120/api/v1/service/es/finance/commodity/update/single";


    private TransactionRecordService transactionRecordService;
    private RestTemplate restTemplate;
    private CommodityService commodityService;
    private StringRedisTemplate stringRedisTemplate;
    private RedissonClient redissonClient;
    public static final AtomicInteger successReplenishRecv = new AtomicInteger(0);
    public static final AtomicInteger failReplenishRecv = new AtomicInteger(0);


    @Transactional
    @Override
    public void onMessage(HashMap<String, Object> msg) {

        Integer userId = (Integer) msg.getOrDefault("user-id", -1);
        Integer commodityId = (Integer) msg.getOrDefault("commodity-id", -1);
        Integer count = (Integer) msg.getOrDefault("count", 0);
        String userIp = (String) msg.getOrDefault("user-ip", "未知IP信息");
        String userAgent = (String) msg.getOrDefault("userAgent", "未知代理信息");

        /*Commodity modifiedCommodity = commodityService.replenishOne(commodityId.longValue(), count);

        Map<String, Object> resMap = new HashMap<>();
        Map<String, Object> mysqlResult = new HashMap<>();
        mysqlResult.put("isUpdated", modifiedCommodity != null);
        resMap.put("mysql_result", mysqlResult);

        if (modifiedCommodity != null) {

            restTemplate.put(ES_UPDATE_COMMODITY_URL, modifiedCommodity);
            resMap.put("es_result", Result.BLANK);

        }*/

        // 2024-6-11  22:38-针对于Redis和MySQL、ElasticSearch秒杀不同步的问题导致二次补货具有不确定性的问题，我们是这样解决的：
        // 补货时，我们按照Long型64位二进制字符串的形式进行补货，例如：补5件商品：  0000 0000 0000 0000 0000 0000 0000 0101  |  0000 0000 0000 0000 0000 0000 0000 0101
        // Redis消费时消费低32位 -> 0000 0000 0000 0000 0000 0000 0000 0101  |  0000 0000 0000 0000 0000 0000 0000 0000，
        // 而MySQL和ElasticSearch消费则消费高32位 -> 0000 0000 0000 0000 0000 0000 0000 0000  |  0000 0000 0000 0000 0000 0000 0000 0000，
        // 每次消费都需要将对应Bit位置0，直至最终变为64个0(转换值为0值)时，本轮补货消费才算结束，否则，只要拿到该Bit位串且其转换为Long型后的值》0,
        // 则表示上一轮的补货还没有完成，依旧拒绝本轮补货
        // 这样即可避免重复补货问题
        // 2024-6-11  20:56-这里只有先前的补货请求被执行后才能进行新一轮的补货操作，以避免数据覆盖问题


        String oldReplenishmentQuantityStr = stringRedisTemplate.opsForValue().get(String.format("replenish:commodity:%d", commodityId));
        // 2024-6-12  00:16-这里之所以我们又把判断条件该为 is null，是因为如果为null时由于网络异常原因导致的，那你后续的Redis操作肯定大概率也会失败，对外表现为啥⑩没有
        // 如果是因为没有这个key，那么意味着本次补货操作是本种商品的第一次补货操作，应予以放行
        // 2024-6-12  00:11-只有在前一次的两轮消费全部完成后(补货值变为64个0时)，才能进行补货，否则会出现一致性问题
        if (oldReplenishmentQuantityStr == null || Integer.parseInt(oldReplenishmentQuantityStr) == 0L) {

            // String countBinaryStr = StringUtils.padToBytes(Integer.toBinaryString(count), '0', 32, StringUtils.Direction.LEFT);
            stringRedisTemplate.opsForValue().set(String.format("replenish:commodity:%d", commodityId), String.valueOf(count));
            log.info("Commodity replenish message sent successfully ! Commodity[{}] Stock Quantity + {}", commodityId, count);
            successReplenishRecv.incrementAndGet();

        } else {

            log.warn("Replenish failed ~ Because there is already a replenishment task");
            failReplenishRecv.incrementAndGet();
            throw new PtpException(808, "商品补货失败", "当前还存在一个正在运行的补货任务，需等待当前补货任务完成后才可添加");

        }

        // log.info("result = {}", resMap);

    }


}
