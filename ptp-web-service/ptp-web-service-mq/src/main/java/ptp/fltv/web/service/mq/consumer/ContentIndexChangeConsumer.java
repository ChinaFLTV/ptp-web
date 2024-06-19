package ptp.fltv.web.service.mq.consumer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import pfp.fltv.common.model.base.content.BaseEntity;
import ptp.fltv.web.service.mq.utils.ContentUtils;

import java.util.HashMap;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/19 PM 10:12:32
 * @description 根据变化后的索引重新计算内容实体的得分的消费者
 * @filename ContentIndexChangeConsumer.java
 */

@AllArgsConstructor
@RocketMQMessageListener(topic = "content-rank-indices-change-topic", consumerGroup = "content-rank-indices-change-consumer-group")
@Slf4j
@Service
public class ContentIndexChangeConsumer implements RocketMQListener<HashMap<String, Object>> {


    private StringRedisTemplate stringRedisTemplate;


    @Override
    public void onMessage(HashMap<String, Object> msg) {

        BaseEntity.ContentType contentType = BaseEntity.ContentType.valueOfByCode((Integer) msg.getOrDefault("content-type", BaseEntity.ContentType.UNKNOWN));
        Integer contentId = (Integer) msg.getOrDefault("content-id", -1);
        Integer views = (Integer) msg.getOrDefault("views", -1);
        Integer likes = (Integer) msg.getOrDefault("likes", -1);
        Integer unlikes = (Integer) msg.getOrDefault("unlikes", -1);
        Integer collects = (Integer) msg.getOrDefault("collects", -1);
        Integer comments = (Integer) msg.getOrDefault("comments", -1);
        Integer lastCollectTimestamp = (Integer) msg.getOrDefault("last-collect-timestamp", -1);

        Double score = ContentUtils.calculateContentScore(views, likes, unlikes, collects, comments, contentId.longValue(), lastCollectTimestamp.longValue());

        stringRedisTemplate.opsForZSet().add(String.format("content:%s:rank100:total", contentType.name().toLowerCase()), String.valueOf(contentId), score);

        log.info("Calculated the score of the {} successfully ! new score is {}", contentType.name().toLowerCase(), score);

    }


}