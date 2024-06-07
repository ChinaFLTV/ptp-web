package ptp.fltv.web.service.mq.consumer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import pfp.fltv.common.exceptions.PtpException;
import pfp.fltv.common.model.po.finance.Commodity;
import pfp.fltv.common.model.po.finance.TransactionRecord;
import pfp.fltv.common.response.Result;
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
 * @description 用于添加商品秒杀订单的消费者
 * @filename CommoditySeckillRecordAddConsumer.java
 */

@AllArgsConstructor
@RocketMQMessageListener(topic = "commodity-seckill-topic", consumerGroup = "commodity-seckill-consumer-group")
@Slf4j
@Service
public class CommoditySeckillRecordAddConsumer implements RocketMQListener<HashMap<String, Object>> {


    private static final String ES_UPDATE_COMMODITY_URL = "http://127.0.0.1:8120/api/v1/service/es/finance/commodity/update/single";


    private TransactionRecordService transactionRecordService;
    private RestTemplate restTemplate;
    private CommodityService commodityService;


    @Transactional
    @Override
    public void onMessage(HashMap<String, Object> msg) {

        Integer userId = (Integer) msg.getOrDefault("user-id", -1);
        Integer commodityId = (Integer) msg.getOrDefault("commodity-id", -1);
        Integer count = (Integer) msg.getOrDefault("count", 0);
        String userIp = (String) msg.getOrDefault("user-ip", "未知IP信息");
        String userAgent = (String) msg.getOrDefault("userAgent", "未知代理信息");

        System.out.println("----------------------------------onMessage-------------------------------");
        System.out.println("msg = " + msg);

        Commodity modifiedCommodity = commodityService.seckillOne(commodityId.longValue(), count);

        Map<String, Object> resMap = new HashMap<>();
        Map<String, Object> mysqlResult = new HashMap<>();
        mysqlResult.put("isUpdated", modifiedCommodity != null);
        resMap.put("mysql_result", mysqlResult);

        if (modifiedCommodity != null) {

            restTemplate.put(ES_UPDATE_COMMODITY_URL, modifiedCommodity);
            resMap.put("es_result", Result.BLANK);

            TransactionRecord record = new TransactionRecord().setUid(userId.longValue())
                    .setCommodityId(modifiedCommodity.getId())
                    .setCount(count)
                    .setTotalPrice(count * modifiedCommodity.getPrice())
                    .setPaymentMode("Wechat")
                    .setTags(modifiedCommodity.getTags())
                    .setCategory(List.of("commodity", "seckill"))
                    .setCreateTime(Timestamp.from(Instant.now()))
                    .setUpdateTime(Timestamp.from(Instant.now()));

            boolean isSaveRecordSuccessfully = transactionRecordService.save(record);
            mysqlResult.put("is_saved_record", isSaveRecordSuccessfully);

            if (!isSaveRecordSuccessfully) {

                throw new PtpException(807, "商品交易记录添加失败");

            }

            log.info("Record add message sent successfully ! TransactionRecord = {}", record.toString());
            log.info("result = {}", resMap);

        } else {

            // 2024-5-24  21:54-MySQL数据库部分执行失败则立即抛异常
            throw new PtpException(807, "商品秒杀失败");

        }


    }


}