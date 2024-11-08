package ptp.fltv.web.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import pfp.fltv.common.enums.ContentQuerySortType;
import pfp.fltv.common.enums.ContentRankType;
import pfp.fltv.common.enums.ContentStatus;
import pfp.fltv.common.model.po.content.Comment;
import pfp.fltv.common.model.po.content.Passage;
import pfp.fltv.common.model.po.manage.Rate;
import pfp.fltv.common.model.po.manage.SubscriberShip;
import pfp.fltv.common.model.po.system.EventRecord;
import ptp.fltv.web.mapper.PassageMapper;
import ptp.fltv.web.service.EventRecordService;
import ptp.fltv.web.service.PassageService;
import ptp.fltv.web.service.RateService;
import ptp.fltv.web.service.SubscriberShipService;

import java.util.*;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 5:06:40
 * @description 文章服务接口的实现类
 * @filename PassageServiceImpl.java
 */

@AllArgsConstructor
@Service
public class PassageServiceImpl extends ServiceImpl<PassageMapper, Passage> implements PassageService {


    private StringRedisTemplate stringRedisTemplate;
    private SubscriberShipService subscriberShipService;
    private RateService rateService;


    @Override
    public List<Passage> getRankListByPage(@Nonnull ContentRankType contentRankType, @Nonnull Long offset, @Nonnull Long count) {

        String key = String.format("content:passage:%s:total", contentRankType.getSubkey());
        Set<String> idsSet = stringRedisTemplate.opsForZSet().reverseRange(key, offset, offset + count - 1);

        if (idsSet == null || idsSet.isEmpty()) {

            return Collections.emptyList();

        }

        // 2024-6-21  21:26-由于BaseMapper::selectBatchIds方法获取到的实体数据会默认按其主键升序排列 , 而我们需要保持数据在排行榜中的数据 , 因此还需要进行一些后处理
        List<Passage> passages = baseMapper.selectBatchIds(idsSet);
        Map<Long, Passage> tempMap = new HashMap<>();

        for (Passage passage : passages) {

            tempMap.put(passage.getId(), passage);

        }

        passages.clear();
        for (String idStr : idsSet) {

            passages.add(tempMap.get(Long.parseLong(idStr)));

        }

        return passages;

    }


    @Override
    public List<Passage> queryPassagePageWithSorting(@Nonnull ContentQuerySortType sortType, @Nonnull Long pageNum, @Nonnull Long pageSize, @Nonnull Long uid) {

        QueryWrapper<Passage> queryWrapper = new QueryWrapper<>();

        switch (sortType) {

            case LATEST -> queryWrapper.orderByDesc("create_time");
            case HOTTEST -> queryWrapper.orderByDesc("like_num")
                    .orderByDesc("star_num")
                    .orderByDesc("comment_num")
                    .orderByDesc("browse_num")
                    .orderByAsc("unlike_num");
            case LIKE -> queryWrapper.orderByDesc("like_num");
            case STAR -> queryWrapper.orderByDesc("star_num");
            case BROWSE -> queryWrapper.orderByDesc("browse_num");
            case COMMENT -> queryWrapper.orderByDesc("comment_num");
            case SHARE -> queryWrapper.orderByDesc("share_num");
            case SUBSCRIBE -> {

                // 2024-10-21  18:22-先查询出当前用户关注了用户的ID集合
                QueryWrapper<SubscriberShip> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("follower_id", uid);
                List<Long> followeeIds = subscriberShipService.list(queryWrapper1)
                        .stream()
                        .map(SubscriberShip::getFolloweeId)
                        .toList();

                queryWrapper.in("uid", followeeIds);

            }
            case RECOMMEND ->
                // 2024-10-21  18:25-TODO-这里目前转换为按照评论量倒序查询 , 后续这里将升级为人工智能根据当前用户的历史浏览记录动态生成并提供用户可能喜欢的文章的元信息
                    queryWrapper.orderByDesc("comment_num");
            case DEFAULT -> {


            }

        }

        // 2024-10-21  16:54-最后兜底的参与排序的字段
        queryWrapper.orderByDesc("id");

        queryWrapper.eq("status", ContentStatus.NORMAL.getCode()); // 2024-11-3  1:20-只筛选出当前处于公开状态的文章(后续将根据请求用户增加选择性展示部分可见性文章)

        List<Passage> passages = page(new Page<>(pageNum, pageSize), queryWrapper).getRecords();

        return passages == null ? new ArrayList<>() : passages;

    }


    @Override
    public List<Passage> queryOperatedPassagePage(@Nonnull EventRecord.EventType eventType, @Nonnull Long uid, @Nonnull Long pageNum, @Nonnull Long pageSize) {

        QueryWrapper<EventRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("event_type", eventType.getCode())
                .eq("uid", uid)
                .eq("content_type", Comment.BelongType.PASSAGE.getCode());

        Page<EventRecord> eventRecordPage = new Page<>(pageNum, pageSize);
        eventRecordPage = SpringUtil.getBean(EventRecordService.class).page(eventRecordPage, queryWrapper); // 2024-10-31  2:22-这里之所以没有直接注入EventRecordService然后使用 , 是因为这样做可能会导致循环依赖

        List<EventRecord> eventRecords = eventRecordPage.getRecords();

        if (eventRecords != null && !eventRecords.isEmpty()) {

            List<Long> passageIds = eventRecords.stream()
                    // 2024-10-31  20:08-由于在拉取数据后再对数据进行按照文章ID集合的顺序重排序比较困难 , 因此这里还是需要进行预先排序以建立文章ID到文章的映射卷积核
                    //// 2024-10-31  19:55-由于不管输入的ID集合是什么顺序的 , MyBatisPlus默认返回ID集合的对应数据实体的集合都是按照数据库的顺序返回的 , 因此这里在返回数据的提前排序是徒劳的 , 应该放到数据返回之后再进行排序
                    .sorted(Comparator.comparingLong(EventRecord::getId).reversed())// 2024-10-31  1:59-这里采用ID进行降序排序 , 因为主键ID与记录创建时间的数值变化趋势是成正相关的 , 在效果上没有差异 , 但是为了效率考量 , 这里选用主键ID
                    .map(EventRecord::getContentId)
                    .toList();

            if (!passageIds.isEmpty()) {

                Map<Long, Passage> map = new HashMap<>();

                List<Passage> passages = listByIds(passageIds);
                if (passages != null && !passages.isEmpty()) {

                    passages.forEach(p -> map.put(p.getId(), p));

                }

                return passageIds.stream()
                        .map(id -> map.getOrDefault(id, null))
                        .filter(p -> p != null && p.getStatus() == ContentStatus.NORMAL) // 2024-11-3  1:22-只筛选出当前处于公开状态的文章(后续将根据请求用户增加选择性展示部分可见性文章)
                        .toList();

            }

        }

        return new ArrayList<>();

    }


    @Override
    public boolean saveSinglePassage(@Nonnull Passage passage) {

        boolean isSaved = false;

        // 2024-10-26  1:47-先新增一条对应的文章评分统计记录
        Rate rate = Rate.builder()
                .uid(passage.getUid())
                .contentType(Comment.BelongType.PASSAGE)
                // .contentId(passage.getId())
                .contentTitle(passage.getTitle())
                .build();

        boolean isSaveRateSuccessfully = rateService.save(rate);

        // 2024-10-26  1:50-只有新增了一条对应的文章评分统计记录之后才能进一步地去创建文章
        if (isSaveRateSuccessfully) {

            passage.setRateId(rate.getId());
            isSaved = save(passage); // 2024-10-26  1:51-即使文章插入失败也不再主动回滚刚刚创建好的评分记录了 , 顶多会产生冗余垃圾 , 不会产生其他副作用 , 可以定期排查删除掉
            if (isSaved) {

                rate = rateService.getById(rate.getId()); // 2024-11-9  2:31-解决直接使用原始构建的rate导致部分字段意外为null从而导致将原先带有数据的部分字段覆盖为null的情况
                rate.setContentId(passage.getId());
                rateService.updateById(rate); // 2024-11-5  1:14-待文章成功插入后 , 再同步更新对应的评分统计记录的内容实体的ID字段

            }

        }

        return isSaved; // 2024-11-5  1:15-只要文章的rateId正确就行 , 评分统计记录的contentId准确与否不作要求(暂时)

    }


    @Override
    public boolean deleteSinglePassage(@Nonnull Long id) {

        boolean isDeleted = false;

        //// 2024-11-8  00:13-这里不再去在业务层对关联的评分统计记录进行额外的删除了 , 因为这已经由数据库的外键的级联删除操作自动完成了
        Passage passage = getById(id);
        if (passage != null) {

            isDeleted = removeById(id);
            rateService.removeById(passage.getRateId());

        }

        return isDeleted;

    }


    @Override
    public List<Passage> queryAvailablePassagePageByUid(@Nonnull Long uid, @Nonnull Long pageNum, @Nonnull Long pageSize) {

        Page<Passage> passagePage = new Page<>(pageNum, pageSize);

        QueryWrapper<Passage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid)
                .eq("status", ContentStatus.NORMAL.getCode());

        passagePage = page(passagePage, queryWrapper);
        return passagePage.getRecords() == null ? new ArrayList<>() : passagePage.getRecords();

    }


}
