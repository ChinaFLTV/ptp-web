package ptp.fltv.web.mq.impl;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;
import pfp.fltv.common.model.base.content.BaseEntity;
import ptp.fltv.web.mq.ContentRankMqService;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/19 PM 12:26:06
 * @description 内容实体排行的MQ服务的实现
 * @filename ContentRankMqServiceImpl.java
 */

@AllArgsConstructor
@Slf4j
@Service
public class ContentRankMqServiceImpl implements ContentRankMqService {


    private RocketMQTemplate rocketMQTemplate;


    @Override
    public void sendIndexChangeMsg(@Nonnull BaseEntity contentObj, @Nonnull BaseEntity.ContentType contentType) {

        Map<String, Object> indices = new HashMap<>();
        indices.put("content-type", contentType);
        indices.put("content-id", contentObj.getId());
        indices.put("views", contentObj.getBrowseNum());
        indices.put("likes", contentObj.getLikeNum());
        indices.put("unlikes", contentObj.getUnlikeNum());
        indices.put("collects", contentObj.getStarNum());
        indices.put("comments", contentObj.getCommentNum());

        Map<String, Object> meta = contentObj.getMeta();
        if (meta != null) {

            indices.put("last-collect-timestamp", meta.getOrDefault("last-collect-timestamp", contentObj.getCreateTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));

        }

        rocketMQTemplate.asyncSend("content-rank-indices-change-topic", indices, null);

    }


}