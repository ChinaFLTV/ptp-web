package ptp.fltv.web.service.mq.consumer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;
import pfp.fltv.common.model.po.finance.TransactionRecord;
import ptp.fltv.web.service.mq.service.TransactionRecordService;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/1 PM 11:22:24
 * @description 用于添加商品秒杀订单的消费者
 * @filename CommoditySeckillRecordAddConsumer.java
 */

@AllArgsConstructor
@RocketMQMessageListener(topic = "commodity-seckill-record-add-topic", consumerGroup = "commodity-seckill-record-consumer-group")
@Slf4j
@Service
public class CommoditySeckillRecordAddConsumer implements RocketMQListener<TransactionRecord> {


    private TransactionRecordService transactionRecordService;


    @Override
    public void onMessage(TransactionRecord transactionRecord) {

        log.info("Record add message sent successfully ! TransactionRecord = {}", transactionRecord.toString());
        boolean isSaveRecordSuccessfully = transactionRecordService.save(transactionRecord);

    }


}