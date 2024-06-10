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
import pfp.fltv.common.model.po.finance.Commodity;
import pfp.fltv.common.response.Result;
import ptp.fltv.web.service.mq.service.CommodityService;
import ptp.fltv.web.service.mq.service.TransactionRecordService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/10 PM 10:01:21
 * @description 用于添加商品补货的消费者
 * @filename CommodityReplenishConsumer.java
 */

@AllArgsConstructor
@RocketMQMessageListener(topic = "commodity-replenish-topic", consumerGroup = "commodity-replenish-consumer-group")
@Slf4j
@Service
public class CommodityReplenishConsumer implements RocketMQListener<HashMap<String, Object>> {


    private static final String ES_UPDATE_COMMODITY_URL = "http://127.0.0.1:8120/api/v1/service/es/finance/commodity/update/single";


    private TransactionRecordService transactionRecordService;
    private RestTemplate restTemplate;
    private CommodityService commodityService;
    private StringRedisTemplate stringRedisTemplate;
    private RedissonClient redissonClient;


    @Transactional
    @Override
    public void onMessage(HashMap<String, Object> msg) {

        Integer userId = (Integer) msg.getOrDefault("user-id", -1);
        Integer commodityId = (Integer) msg.getOrDefault("commodity-id", -1);
        Integer count = (Integer) msg.getOrDefault("count", 0);
        String userIp = (String) msg.getOrDefault("user-ip", "未知IP信息");
        String userAgent = (String) msg.getOrDefault("userAgent", "未知代理信息");

        Commodity modifiedCommodity = commodityService.replenishOne(commodityId.longValue(), count);

        Map<String, Object> resMap = new HashMap<>();
        Map<String, Object> mysqlResult = new HashMap<>();
        mysqlResult.put("isUpdated", modifiedCommodity != null);
        resMap.put("mysql_result", mysqlResult);

        if (modifiedCommodity != null) {

            restTemplate.put(ES_UPDATE_COMMODITY_URL, modifiedCommodity);
            resMap.put("es_result", Result.BLANK);

        }

        log.info("Commodity replenish message sent successfully ! Commodity[{}] Stock Quantity + {}", commodityId, count);
        log.info("result = {}", resMap);

    }


}
