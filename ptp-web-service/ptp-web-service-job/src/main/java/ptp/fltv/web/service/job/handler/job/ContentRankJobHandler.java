package ptp.fltv.web.service.job.handler.job;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import pfp.fltv.common.model.base.content.BaseEntity;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/20 PM 9:00:32
 * @description 与内容实体排名相关的数据操作任务
 * @filename ContentRankJobHandler.java
 */

@AllArgsConstructor
@Component
public class ContentRankJobHandler {


    private StringRedisTemplate stringRedisTemplate;
    private final Integer TOTAL_SIZE = 100; // 2024-6-20  21:40-排行榜总容纳数量
    private final Integer RETAIN_QUANTITY = TOTAL_SIZE + TOTAL_SIZE * 2 / 3; // 2024-6-20  22:11-最终实际要保留下来的排行榜条目数
    private final Integer EVICTION_QUANTITY = 10_000; // 2024-6-20  22:04-单次清理的条目数
    private final BaseEntity.ContentType[] contentTypes = {BaseEntity.ContentType.ANNOUNCEMENT,
            BaseEntity.ContentType.DIALOGUE, BaseEntity.ContentType.PASSAGE, BaseEntity.ContentType.PASSAGE_COMMENT}; // 2024-6-20  21:41-内容实体类型数组
    private final AtomicInteger curContentTypeIndex = new AtomicInteger(0); // 2024-6-20  21:41-当前需要排查的内容实体类型索引


    @XxlJob("trim-total100rank-size-handler")
    public void trimTotal100RankSize() {

        XxlJobHelper.log("Start the Total-100 leaderboard quantity cropping task...");

        String key = String.format("content:%s:rank100:total", contentTypes[curContentTypeIndex.get()].name().toLowerCase());
        Long size = stringRedisTemplate.opsForZSet().zCard(key);

        if (size == null) {

            XxlJobHelper.handleFail("An exception occurred when the Redis command was executed ~");
            return;

        }

        // 2024-6-20  21:34-虽然我们规定只能保留100条排行榜条目，但是我们同时还有预留出2/3也就是大约66条的排行榜条目用以备用
        // 为什么这么做?
        // 因为可能会存在排行榜前100的内容实体的作者突然间做出 删除内容 的诡异决定，因此为了避免可能会发生的短时间的排行榜空缺情况，我们必须要提前预留出2/3的排行榜下层条目以及时地补位
        if (size > RETAIN_QUANTITY) {

            // 2024-6-20  21:22-此处操作并不会存在并发性问题，因为对于指令的接收、解析、执行、结果返回均是单线程的
            // 补充：当然从整个Redis层面来说，它并不是单线程的
            // Redis v2.6+ : +2*子线程 : 关闭文件线程 & AOF刷盘线程
            // Redis v4.0+ : +1*后台(lazyfree)线程 : 用来异步释放Redis内存

            // 2024-6-20  21:29-注意一种极端情况 : 可能会出现在我们的任务执行间隔内排行榜增加了数百万条数据，因此，我们裁剪起来必定会耗费大量时间
            // 因此，我们应该每次清理仅清理固定数量的条目，不多也不少(客户端那边不会一直产生大量的排行榜数据的，因为上榜的最低要求是——得分必须大于当前排行榜的最低得分)

            Long removedRange = stringRedisTemplate.opsForZSet().removeRange(key, Math.max(RETAIN_QUANTITY, size - EVICTION_QUANTITY), size - 1);

            if (removedRange == null) {

                XxlJobHelper.handleFail("The Total-100 leaderboard quantity cropping task failed to execute ~");

            } else {

                curContentTypeIndex.set((curContentTypeIndex.get() + 1) % contentTypes.length);
                XxlJobHelper.handleSuccess(String.format("Complete the Total-100 leaderboard quantity cropping task successfully : removeRange = %d", removedRange));

            }

        } else {

            XxlJobHelper.handleSuccess(String.format("The current number of the leaderboard entries is below the preset value , so no trimming is required : size = %d", size));

        }

    }


}