package ptp.fltv.web.service.job.worker.impl;

import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import pfp.fltv.common.exceptions.PtpException;
import ptp.fltv.web.service.job.worker.IdWorker;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/19 PM 10:45:34
 * @description 用于进行ID相关数据操作的Redis工人实现
 * @filename RedisIdWorker.java
 */

@Component
public class RedisIdWorker implements IdWorker {


    private static final long BEGIN_TIMESTAMP = 1716134400L;// 2024-5-19  21:53-ID起始时间(second)：2024/5/20  00:00:00
    private static final int SEQUENCE_NUMBER_BITS = 32;// 2024-5-19  21:49-序列号占用的位数
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public long generateGoodsKillId(@Nonnull String infix) throws PtpException {

        LocalDateTime now = LocalDateTime.now();
        long currentTimestamp = now.toEpochSecond(ZoneOffset.UTC);
        String date = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Long currentSequenceNumber = stringRedisTemplate.opsForValue().increment(String.format("goods:kill:%s:%s", infix, date));

        if (currentSequenceNumber == null) {

            throw new PtpException(805);

        }

        return currentTimestamp << SEQUENCE_NUMBER_BITS | currentSequenceNumber;

    }


}
