package ptp.fltv.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import pfp.fltv.common.enums.ContentRankType;
import pfp.fltv.common.model.po.content.Passage;
import ptp.fltv.web.mapper.PassageMapper;
import ptp.fltv.web.service.PassageService;

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


}
