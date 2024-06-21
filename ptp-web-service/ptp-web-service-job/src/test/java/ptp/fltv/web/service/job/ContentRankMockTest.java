package ptp.fltv.web.service.job;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import pfp.fltv.common.utils.ContentUtils;

import java.util.Random;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/20 PM 10:24:42
 * @description 增加实体排行榜的虚拟条目数据的测试
 * @filename ContentRankMockTest.java
 */

@Slf4j
@SpringBootTest
public class ContentRankMockTest {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    // 2024-6-21  8:54-注意请使用 org.junit.jupiter.api.Test 而不是 org.junit.Test , 否则会出现Bean无法自动注入的异常情况!!!
    @Test
    public void addMockRankData() {

        log.info("Start to add simulated data to the leaderboard...");

        final int RANK_DATA_QUANTITY = 10_000; // 2024-6-20  22:50-测试将会产生的排行榜条目数
        final String RANK_KEY = "content:passage:rank100:total"; // 2024-6-20  22:50-针对的排行榜的键名
        final Random random = new Random(666);

        // 2024-6-20  23:41-采用Redis的Pipeline技术来加速指令的执行
        // 管道技术(Pipeline)是客户端(非服务端)提供的一种批处理技术，用于一次性处理多个Redis命令，从而提高了交互性能
        // 同时该技术也解决了执行多条Redis指令时产生的网络等待，它通过将本地客户端的多条Redis指令整合到一起之后再统一发送给Redis服务端去处理，然后全部执行完成后再统一返回
        // 这样就避免了每条Redis指令执行后都需要等待的情况(即下一条指令必须得等到客户端接收到上一条指令的执行结果后才能发送，这个等待过程是1RTT)，从而提高了程序的执行效率
        // 注意 : 尽量不要发送数据量过大的指令，这样可能会引起短时间内的网络阻塞
        stringRedisTemplate.executePipelined(new SessionCallback<>() {


            @SuppressWarnings("unchecked")
            @Override
            public <K, V> Object execute(@Nonnull RedisOperations<K, V> operations) throws DataAccessException {

                for (int i = 0; i < RANK_DATA_QUANTITY; i++) {

                    int id = random.nextInt(1_000_000_000) + 999_999;
                    int views = random.nextInt(10000) + 500;
                    int likes = random.nextInt(10000) + 999;
                    int unlikes = random.nextInt(10000) + 200;
                    int collects = random.nextInt(10000) + 666;
                    int comments = random.nextInt(10000) + 520;
                    int createElapseTimestamp = random.nextInt(7 * 24 * 60 * 60 * 1000) + 1314;
                    int lastCommentElapseTimestamp = random.nextInt(7 * 24 * 60 * 60 * 1000) + 520;
                    long currentTimestamp = System.currentTimeMillis();

                    Double score = ContentUtils.calculateContentScore(views, likes, unlikes, collects, comments, currentTimestamp - createElapseTimestamp, currentTimestamp - lastCommentElapseTimestamp);

                    operations.opsForZSet().add((K) RANK_KEY, (V) String.valueOf(id), score);

                }
                return null;

            }


        });

        log.info("The task of adding simulated leaderboard data is complete !");

    }


}
