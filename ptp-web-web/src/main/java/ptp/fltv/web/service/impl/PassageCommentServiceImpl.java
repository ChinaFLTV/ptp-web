package ptp.fltv.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import pfp.fltv.common.enums.ContentRankType;
import pfp.fltv.common.model.po.content.PassageComment;
import ptp.fltv.web.mapper.PassageCommentMapper;
import ptp.fltv.web.service.PassageCommentService;

import java.util.*;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 6:41:50
 * @description 文章评论服务接口的实现类
 * @filename PassageCommentServiceImpl.java
 */

@AllArgsConstructor
@Service
public class PassageCommentServiceImpl extends ServiceImpl<PassageCommentMapper, PassageComment> implements PassageCommentService {


    private StringRedisTemplate stringRedisTemplate;


    @Override
    public List<PassageComment> getRankListByPage(@Nonnull ContentRankType contentRankType, @Nonnull Long offset, @Nonnull Long count) {

        String key = String.format("content:passage:comment:%s:total", contentRankType.getSubkey());
        Set<String> idsSet = stringRedisTemplate.opsForZSet().reverseRange(key, offset, offset + count - 1);

        if (idsSet == null || idsSet.isEmpty()) {

            return Collections.emptyList();

        }

        // 2024-6-21  21:27-由于BaseMapper::selectBatchIds方法获取到的实体数据会默认按其主键升序排列 , 而我们需要保持数据在排行榜中的数据 , 因此还需要进行一些后处理
        List<PassageComment> passageComments = baseMapper.selectBatchIds(idsSet);
        Map<Long, PassageComment> tempMap = new HashMap<>();

        for (PassageComment passageComment : passageComments) {

            tempMap.put(passageComment.getId(), passageComment);

        }

        passageComments.clear();
        for (String idStr : idsSet) {

            passageComments.add(tempMap.get(Long.parseLong(idStr)));

        }

        return passageComments;


    }


}
