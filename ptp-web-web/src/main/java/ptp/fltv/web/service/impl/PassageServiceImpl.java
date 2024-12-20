package ptp.fltv.web.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfp.fltv.common.enums.ContentQuerySortType;
import pfp.fltv.common.enums.ContentRankType;
import pfp.fltv.common.enums.ContentStatus;
import pfp.fltv.common.exceptions.PtpException;
import pfp.fltv.common.model.po.content.Comment;
import pfp.fltv.common.model.po.content.Passage;
import pfp.fltv.common.model.po.manage.Asset;
import pfp.fltv.common.model.po.manage.Rate;
import pfp.fltv.common.model.po.manage.SubscriberShip;
import pfp.fltv.common.model.po.manage.User;
import pfp.fltv.common.model.po.system.EventRecord;
import ptp.fltv.web.mapper.PassageMapper;
import ptp.fltv.web.service.*;

import java.util.*;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 5:06:40
 * @description 文章服务接口的实现类
 * @filename PassageServiceImpl.java
 */

@Slf4j
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

        QueryWrapper<Passage> queryWrapper;
        try {

            queryWrapper = sortType2QueryWrapper(sortType, uid);

        } catch (PtpException ex) {

            log.error("根据关注用户类型进行分页查询失败 : 用户没有关注任何其他用户({})", ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());

            // 2024-11-25  14:53-这里捕获排序类型为用户关注且用户没有任何关注对象的情况 , 因此 , 既然用户没有关注任何其他用户 , 那么自然也就不会拉取到任何其他文章 , 因此这里出现这种情况直接进行返回
            return new ArrayList<>();

        }

        List<Passage> passages = page(new Page<>(pageNum, pageSize), queryWrapper).getRecords();

        return passages == null ? new ArrayList<>() : passages;

    }


    @Override
    public List<Passage> queryPassagePageWithSortingFuzzily(@Nonnull ContentQuerySortType sortType, @Nonnull String title, @Nonnull Long pageNum, @Nonnull Long pageSize, @Nonnull Long uid) {

        QueryWrapper<Passage> queryWrapper;
        try {

            queryWrapper = sortType2QueryWrapper(sortType, uid);

        } catch (PtpException ex) {

            log.error("根据关注用户类型进行分页查询失败 : {}", ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());

            // 2024-11-25  14:53-这里捕获排序类型为用户关注且用户没有任何关注对象的情况 , 因此 , 既然用户没有关注任何其他用户 , 那么自然也就不会拉取到任何其他文章 , 因此这里出现这种情况直接进行返回
            return new ArrayList<>();

        }

        queryWrapper.like("title", title);

        List<Passage> passages = page(new Page<>(pageNum, pageSize), queryWrapper).getRecords();

        return passages == null ? new ArrayList<>() : passages;

    }


    /**
     * @param sortType 文章排序类型
     * @param uid      当前请求发起用户的ID(非必需)(仅在排序类型为订阅类型下生效)
     * @return 文章排序对应的查询包装器
     * @author Lenovo/LiGuanda
     * @date 2024/11/22 PM 8:50:10
     * @version 1.0.0
     * @description 根据输入的文章排序类型来生成对应的查询包装器
     * @filename PassageServiceImpl.java
     */
    private QueryWrapper<Passage> sortType2QueryWrapper(ContentQuerySortType sortType, Long uid) {

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

                // 2024-11-25  14:50-这里主要解决当用户没有关注任何其他用户时 , 此时再去套用IN语法就会出错的情况
                if (followeeIds.isEmpty()) {

                    throw new PtpException(-1, "用户没有关注任何其他用户!");

                }

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

        return queryWrapper;

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

                // 2024-11-11  19:19-添加文章成功后自动给发布者用户增加0.4积分(该积分增加操作失败也不会告知用户 , 毕竟这几乎不会产生副作用(可能就用户自己少赚得一点积分...))
                EventRecordService eventRecordService = SpringUtil.getBean(EventRecordService.class);
                AssetService assetService = SpringUtil.getBean(AssetService.class);
                UserService userService = SpringUtil.getBean(UserService.class);

                User user = userService.getById(passage.getUid());
                if (user != null) {

                    Map<String, Object> meta = new HashMap<>();
                    meta.put("quantity", 0.4);

                    EventRecord eventRecord = EventRecord.builder()
                            .uid(user.getId())
                            .nickname(user.getNickname())
                            .avatarUrl(JSON.parseObject(user.getAvatar()).getString("uri"))
                            .contentType(Comment.BelongType.ASSET)
                            .contentId(user.getAssetId())
                            .eventType(EventRecord.EventType.EARN)
                            .remark("因 发表一篇文章(id = %d) 而 获得 0.4 积分".formatted(passage.getId()))
                            .meta(meta)
                            .build();

                    boolean isSavedEventRecord = eventRecordService.save(eventRecord);
                    if (isSavedEventRecord) {

                        Asset asset = assetService.getById(user.getAssetId());
                        if (asset != null) {

                            asset.setBalance(asset.getBalance() + 0.4);
                            assetService.updateById(asset);

                        }

                    }

                }

            }

        }

        return isSaved; // 2024-11-5  1:15-只要文章的rateId正确就行 , 评分统计记录的contentId准确与否不作要求(暂时)

    }


    @Transactional
    @Override
    public boolean updateSinglePassage(Passage passage) {

        Passage oldPassage = getById(passage.getId());

        // 2024-11-11  20:13-这里通过先删除后增加的方式去实现对文章的"更新" , 这样做能够保留修改前的文章副本
        boolean isDeleted = removeById(passage.getId());

        boolean isInserted = false;
        if (isDeleted) {

            passage.setId(null);
            isInserted = save(passage);

            // 2024-11-11  19:59-如果文章修改成功 , 则还需要同步增加一条文章修改操作的记录
            if (isInserted) {

                EventRecordService eventRecordService = SpringUtil.getBean(EventRecordService.class);
                UserService userService = SpringUtil.getBean(UserService.class);

                User user = userService.getById(passage.getUid());
                if (user != null) {

                    oldPassage.setContent(null); // 2024-11-11  20:46-由于事件记录需要记录历史文章的数据 , 而内容本体大多数情况下体积较大 , 而列表展示的时候又基本上用不到 , 因此这里不再额外存储旧文章的内容本体(况且 , 文章表也存放着历史文章数据 , 只不过是被逻辑删除了而已)

                    EventRecord eventRecord = EventRecord.builder()
                            .uid(user.getId())
                            .nickname(user.getNickname())
                            .avatarUrl(JSON.parseObject(user.getAvatar()).getString("uri"))
                            .contentType(Comment.BelongType.PASSAGE)
                            .contentId(passage.getId())
                            .contentTitle(passage.getTitle())
                            .eventType(EventRecord.EventType.UPDATE)
                            .remark(JSON.toJSONString(oldPassage))
                            .build();

                    eventRecordService.save(eventRecord); // 2024-11-11  20:00-不关心文章修改记录有没有插入成功的结果 , 文章本身是否更新成功才是重中之重

                }

            }

        }

        return isInserted;

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
