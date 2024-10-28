package ptp.fltv.web.service.impl;

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
import ptp.fltv.web.mapper.PassageMapper;
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

        List<Passage> passages = page(new Page<>(pageNum, pageSize), queryWrapper).getRecords();

        return passages == null ? new ArrayList<>() : passages;

    }


    @Override
    public boolean saveSinglePassage(@Nonnull Passage passage) {

        boolean isSaved = false;

        // 2024-10-26  1:47-先新增一条对应的文章评分统计记录
        Rate rate = Rate.builder()
                .uid(passage.getUid())
                .contentType(Comment.BelongType.PASSAGE)
                .contentId(passage.getId())
                .contentTitle(passage.getTitle())
                .build();

        boolean isSaveRateSuccessfully = rateService.save(rate);

        // 2024-10-26  1:50-只有新增了一条对应的文章评分统计记录之后才能进一步地去创建文章
        if (isSaveRateSuccessfully) {

            passage.setRateId(rate.getId());
            isSaved = save(passage); // 2024-10-26  1:51-即使文章插入失败也不再主动回滚刚刚创建好的评分记录了 , 顶多会产生冗余垃圾 , 不会产生其他副作用 , 可以定期排查删除掉

        }

        return isSaved;

    }


    @Override
    public boolean deleteSinglePassage(@Nonnull Long id) {

        boolean isDeleted = false;

        Passage passage = getById(id);
        if (passage != null) {

            isDeleted = removeById(id);
            rateService.removeById(passage.getRateId());

        }

        return isDeleted;

    }


    @Override
    public List<Passage> queryAvailablePassagePageByUid(@Nonnull Long uid, @Nonnull Long offset, @Nonnull Long limit) {

        Page<Passage> passagePage = new Page<>(offset, limit);

        QueryWrapper<Passage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid)
                .eq("status", ContentStatus.NORMAL.getCode());

        passagePage = page(passagePage, queryWrapper);
        return passagePage.getRecords() == null ? new ArrayList<>() : passagePage.getRecords();

    }


}
