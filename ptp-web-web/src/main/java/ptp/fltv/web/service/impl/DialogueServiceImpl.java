package ptp.fltv.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import pfp.fltv.common.enums.ContentRankType;
import pfp.fltv.common.model.po.content.Dialogue;
import ptp.fltv.web.mapper.DialogueMapper;
import ptp.fltv.web.service.DialogueService;

import java.util.*;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/22 下午 6:18:31
 * @description 对话服务接口的实现类
 * @filename DialogueServiceImpl.java
 */

@AllArgsConstructor
@Service
public class DialogueServiceImpl extends ServiceImpl<DialogueMapper, Dialogue> implements DialogueService {


    private StringRedisTemplate stringRedisTemplate;


    @Override
    public List<Dialogue> getRankListByPage(@Nonnull ContentRankType contentRankType, @Nonnull Long offset, @Nonnull Long count) {

        String key = String.format("content:dialogue:%s:total", contentRankType.getSubkey());
        Set<String> idsSet = stringRedisTemplate.opsForZSet().reverseRange(key, offset, offset + count - 1);

        if (idsSet == null || idsSet.isEmpty()) {

            return Collections.emptyList();

        }

        // 2024-6-21  21:24-由于BaseMapper::selectBatchIds方法获取到的实体数据会默认按其主键升序排列 , 而我们需要保持数据在排行榜中的数据 , 因此还需要进行一些后处理
        List<Dialogue> dialogues = baseMapper.selectBatchIds(idsSet);
        Map<Long, Dialogue> tempMap = new HashMap<>();

        for (Dialogue dialogue : dialogues) {

            tempMap.put(dialogue.getId(), dialogue);

        }

        dialogues.clear();
        for (String idStr : idsSet) {

            dialogues.add(tempMap.get(Long.parseLong(idStr)));

        }

        return dialogues;


    }


}
