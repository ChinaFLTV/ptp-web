package ptp.fltv.web.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pfp.fltv.common.exceptions.PtpException;
import pfp.fltv.common.model.po.content.Comment;
import pfp.fltv.common.model.po.content.Passage;
import pfp.fltv.common.model.po.manage.Asset;
import pfp.fltv.common.model.po.manage.Rate;
import pfp.fltv.common.model.po.manage.User;
import pfp.fltv.common.model.po.system.EventRecord;
import ptp.fltv.web.mapper.RateMapper;
import ptp.fltv.web.service.AssetService;
import ptp.fltv.web.service.EventRecordService;
import ptp.fltv.web.service.RateService;
import ptp.fltv.web.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/10/26 AM 12:20:03
 * @description 评分服务接口的实现类
 * @filename RateServiceImpl.java
 */

@RequiredArgsConstructor
@Service
public class RateServiceImpl extends ServiceImpl<RateMapper, Rate> implements RateService {


    @Override
    public Rate querySingleUserRate(@Nonnull Long contentId, @Nonnull Long uid) {

        QueryWrapper<Rate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("content_id", contentId);
        queryWrapper.eq("uid", uid);

        return getOne(queryWrapper);

    }


    @Override
    public boolean insertSingleRate(@Nonnull Rate rate) {

        initializeRate(rate); // 2024-11-8  1:05-先填充一些值为null且可能会引发空指针异常的字段数据

        // 2024-10-27  17:17-首先先判断当前评分记录的类型
        switch (rate.getRateType()) {

            case CONTENT_STATISTIC -> {

                // 2024-10-27  17:18-如果是文章评分统计记录 , 那么这种情况最好处理 , 直接将该评分统计记录插入到数据库中返回即可
                boolean isSaved = save(rate);

                // 2024-11-11  19:51-添加评分成功后自动给发布者用户增加0.1积分(该积分增加操作失败也不会告知用户 , 毕竟这几乎不会产生副作用(可能就用户自己少赚得一点积分...))
                EventRecordService eventRecordService = SpringUtil.getBean(EventRecordService.class);
                AssetService assetService = SpringUtil.getBean(AssetService.class);
                UserService userService = SpringUtil.getBean(UserService.class);

                User user = userService.getById(rate.getUid());
                if (user != null) {

                    EventRecord eventRecord = EventRecord.builder()
                            .uid(user.getId())
                            .nickname(user.getNickname())
                            .avatarUrl(JSON.parseObject(user.getAvatar()).getString("uri"))
                            .contentType(Comment.BelongType.ASSET)
                            .contentId(user.getAssetId())
                            .eventType(EventRecord.EventType.EARN)
                            .remark("因 发布一条评分(id = %d) 而 获得 0.1 积分".formatted(rate.getId()))
                            .build();

                    boolean isSavedEventRecord = eventRecordService.save(eventRecord);
                    if (isSavedEventRecord) {

                        Asset asset = assetService.getById(user.getAssetId());
                        if (asset != null) {

                            asset.setBalance(asset.getBalance() + 0.1);
                            assetService.updateById(asset);

                        }

                    }

                }

                return isSaved;

            }

            case USER_RATE -> {

                // 2024-10-7  17:21-这种情况稍微复杂些 , 需要分下面几步进行 :
                // 2024-10-27  17:22-首先查看当前用户是否已经评分过该内容实体 , 如果评分过则直接快速失败以终止本次评分插入过程
                Rate userRate = querySingleUserRate(rate.getContentId(), rate.getUid());
                if (userRate == null) {

                    // 2024-10-27  17:25-若用户之前没有评分过 , 则现在直接插入 , 然后再根据插入结果同步更新对应的内容实体的评分统计记录数据
                    boolean isSaved = save(rate);

                    if (isSaved) {

                        // 2024-10-27  17:29-插入成功后 , 需要根据刚刚插入的评分记录重新计算并更新对应的实体的统计数据记录
                        Rate statisticRate = getById(rate.getStatisticRateId()); // 2024-10-27  17:38-用户评分记录必须确保该statisticRateId字段不为空!!!
                        if (statisticRate != null) {

                            double userAverageScore = rate.getRateMap()
                                    .values()
                                    .stream()
                                    .mapToDouble(Double::doubleValue)
                                    .average()
                                    .orElse(0D);

                            double userMaxScore = rate.getRateMap()
                                    .values()
                                    .stream()
                                    .mapToDouble(Double::doubleValue)
                                    .max()
                                    .orElse(0D);

                            double userMinScore = rate.getRateMap()
                                    .values()
                                    .stream()
                                    .mapToDouble(Double::doubleValue)
                                    .min()
                                    .orElse(0D);

                            statisticRate.setContentTags(rate.getContentTags());
                            // 2024-10-27  17:54-这里让用户平均评分参与到内容实体的最终评分累计中去
                            double newAverageScore = (statisticRate.getAverageScore() * statisticRate.getRateUserCount() + userAverageScore) / (statisticRate.getRateUserCount() + 1);
                            statisticRate.setAverageScore(newAverageScore);

                            statisticRate.setMaxScore(Math.max(statisticRate.getMaxScore(), userMaxScore));
                            statisticRate.setMinScore(Math.min(statisticRate.getMaxScore(), userMinScore));

                            // 2024-10-27  17:55-这里让用户平均评分参与到内容实体的累计评分分布映射中去
                            if (statisticRate.getRateUserCountMap() == null) {

                                statisticRate.setRateUserCountMap(new HashMap<>());

                            }
                            statisticRate.getRateUserCountMap().compute((int) userAverageScore + ".X", (k, v) -> v == null ? 1 : v + 1);

                            if (statisticRate.getRateMap() == null) {

                                statisticRate.setRateMap(new HashMap<>());

                            }

                            statisticRate.getRateMap().compute(Passage.Standard.AUTHENTICITY.name(), (k, v) -> v == null ? rate.getRateMap().getOrDefault(Passage.Standard.AUTHENTICITY.name(), 0D) :
                                    ((statisticRate.getRateMap().getOrDefault(Passage.Standard.AUTHENTICITY.name(), 0D) * statisticRate.getRateUserCount() + rate.getRateMap().getOrDefault(Passage.Standard.AUTHENTICITY.name(), 0D)) / (statisticRate.getRateUserCount() + 1)));
                            statisticRate.getRateMap().compute(Passage.Standard.ACCURACY.name(), (k, v) -> v == null ? rate.getRateMap().getOrDefault(Passage.Standard.ACCURACY.name(), 0D) :
                                    ((statisticRate.getRateMap().getOrDefault(Passage.Standard.ACCURACY.name(), 0D) * statisticRate.getRateUserCount() + rate.getRateMap().getOrDefault(Passage.Standard.ACCURACY.name(), 0D)) / (statisticRate.getRateUserCount() + 1)));
                            statisticRate.getRateMap().compute(Passage.Standard.OBJECTIVITY.name(), (k, v) -> v == null ? rate.getRateMap().getOrDefault(Passage.Standard.OBJECTIVITY.name(), 0D) :
                                    ((statisticRate.getRateMap().getOrDefault(Passage.Standard.OBJECTIVITY.name(), 0D) * statisticRate.getRateUserCount() + rate.getRateMap().getOrDefault(Passage.Standard.OBJECTIVITY.name(), 0D)) / (statisticRate.getRateUserCount() + 1)));
                            statisticRate.getRateMap().compute(Passage.Standard.DEPTH.name(), (k, v) -> v == null ? rate.getRateMap().getOrDefault(Passage.Standard.DEPTH.name(), 0D) :
                                    ((statisticRate.getRateMap().getOrDefault(Passage.Standard.DEPTH.name(), 0D) * statisticRate.getRateUserCount() + rate.getRateMap().getOrDefault(Passage.Standard.DEPTH.name(), 0D)) / (statisticRate.getRateUserCount() + 1)));
                            statisticRate.getRateMap().compute(Passage.Standard.LOGICALITY.name(), (k, v) -> v == null ? rate.getRateMap().getOrDefault(Passage.Standard.LOGICALITY.name(), 0D) :
                                    ((statisticRate.getRateMap().getOrDefault(Passage.Standard.LOGICALITY.name(), 0D) * statisticRate.getRateUserCount() + rate.getRateMap().getOrDefault(Passage.Standard.LOGICALITY.name(), 0D)) / (statisticRate.getRateUserCount() + 1)));
                            statisticRate.getRateMap().compute(Passage.Standard.TIMELINESS.name(), (k, v) -> v == null ? rate.getRateMap().getOrDefault(Passage.Standard.TIMELINESS.name(), 0D) :
                                    ((statisticRate.getRateMap().getOrDefault(Passage.Standard.TIMELINESS.name(), 0D) * statisticRate.getRateUserCount() + rate.getRateMap().getOrDefault(Passage.Standard.TIMELINESS.name(), 0D)) / (statisticRate.getRateUserCount() + 1)));

                            statisticRate.setRateUserCount(statisticRate.getRateUserCount() + 1);

                            boolean isUpdated = updateById(statisticRate);

                            // 2024-10-27  20:29-如果更新内容评分统计记录失败 , 则删除刚刚插入的那一条用户评分记录以避免用户失败后再次评分报 用户已经评分过 的异常
                            if (!isUpdated) {

                                removeById(rate.getId());
                                return false;

                            } else {

                                // 2024-11-11  19:52-添加评分成功后自动给发布者用户增加0.1积分(该积分增加操作失败也不会告知用户 , 毕竟这几乎不会产生副作用(可能就用户自己少赚得一点积分...))
                                EventRecordService eventRecordService = SpringUtil.getBean(EventRecordService.class);
                                AssetService assetService = SpringUtil.getBean(AssetService.class);
                                UserService userService = SpringUtil.getBean(UserService.class);

                                User user = userService.getById(rate.getUid());
                                if (user != null) {

                                    EventRecord eventRecord = EventRecord.builder()
                                            .uid(user.getId())
                                            .nickname(user.getNickname())
                                            .avatarUrl(JSON.parseObject(user.getAvatar()).getString("uri"))
                                            .contentType(Comment.BelongType.ASSET)
                                            .contentId(user.getAssetId())
                                            .eventType(EventRecord.EventType.EARN)
                                            .remark("因 发布一条评分(id = %d) 而 获得 0.1 积分".formatted(rate.getId()))
                                            .build();

                                    boolean isSavedEventRecord = eventRecordService.save(eventRecord);
                                    if (isSavedEventRecord) {

                                        Asset asset = assetService.getById(user.getAssetId());
                                        if (asset != null) {

                                            asset.setBalance(asset.getBalance() + 0.1);
                                            assetService.updateById(asset);

                                        }

                                    }

                                }

                                return true;

                            }

                        }

                    }

                    return isSaved; //// 2024-10-27  17:27-这里只关注用户评分记录插入成功的结果 , 用户插入成功而更新内容统计记录数据失败的结果忽略 , 大不了用户再评分一次罢了(但是绝对不能出现先同步后插入的情况 , 这种会导致后期数据排查困难(最起码先插入失败后的数据只是浮动垃圾而已))

                } else {

                    throw new PtpException(817, "重复评分!");

                }

            }

            default -> {

                return false;

            }

        }

    }


    /**
     * @param rate 需要被进行字段初始化的评分统计记录
     * @author Lenovo/LiGuanda
     * @date 2024/11/8 AM 12:56:40
     * @version 1.0.0
     * @description 填充Rate中为null的可能会导致空指针异常的字段
     * @filename RateServiceImpl.java
     */
    private void initializeRate(Rate rate) {

        if (rate.getContentTags() == null) {

            rate.setContentTags(new ArrayList<>());

        }

        if (rate.getRateUserCountMap() == null) {

            Map<String, Integer> rateUserCountMap = new HashMap<>();
            for (int i = 0; i <= 10; i++) {

                rateUserCountMap.put(i + ".X", 0);

            }
            rate.setRateUserCountMap(rateUserCountMap);

        }

        if (rate.getRateMap() == null) {

            Map<String, Double> rateMap = new HashMap<>();
            rateMap.put(Passage.Standard.AUTHENTICITY.name(), 0.0);
            rateMap.put(Passage.Standard.ACCURACY.name(), 0.0);
            rateMap.put(Passage.Standard.OBJECTIVITY.name(), 0.0);
            rateMap.put(Passage.Standard.DEPTH.name(), 0.0);
            rateMap.put(Passage.Standard.LOGICALITY.name(), 0.0);
            rateMap.put(Passage.Standard.TIMELINESS.name(), 0.0);
            rate.setRateMap(rateMap);

        }

    }


}