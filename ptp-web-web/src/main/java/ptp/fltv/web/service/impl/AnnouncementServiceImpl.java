package ptp.fltv.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import pfp.fltv.common.enums.ContentRankType;
import pfp.fltv.common.model.po.content.Announcement;
import ptp.fltv.web.mapper.AnnouncementMapper;
import ptp.fltv.web.service.AnnouncementService;

import java.util.*;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 6:09:14
 * @description 公告服务接口的实现类
 * @filename AnnouncementServiceImpl.java
 */

@AllArgsConstructor
@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {


    private StringRedisTemplate stringRedisTemplate;


    @Override
    public List<Announcement> getRankListByPage(@Nonnull ContentRankType contentRankType, @Nonnull Long offset, @Nonnull Long count) {

        String key = String.format("content:announcement:%s:total", contentRankType.getSubkey());
        Set<String> idsSet = stringRedisTemplate.opsForZSet().reverseRange(key, offset, offset + count - 1);

        if (idsSet == null || idsSet.isEmpty()) {

            return Collections.emptyList();

        }

        // 2024-6-21  21:21-由于BaseMapper::selectBatchIds方法获取到的实体数据会默认按其主键升序排列 , 而我们需要保持数据在排行榜中的数据 , 因此还需要进行一些后处理
        List<Announcement> announcements = baseMapper.selectBatchIds(idsSet);
        Map<Long, Announcement> tempMap = new HashMap<>();

        for (Announcement announcement : announcements) {

            tempMap.put(announcement.getId(), announcement);

        }
        announcements.clear();
        for (String idStr : idsSet) {

            announcements.add(tempMap.get(Long.parseLong(idStr)));

        }

        return announcements;

    }


}
