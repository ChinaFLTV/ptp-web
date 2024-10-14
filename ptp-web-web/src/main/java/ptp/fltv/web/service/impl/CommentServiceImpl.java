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
import pfp.fltv.common.model.po.content.Comment;
import ptp.fltv.web.mapper.CommentMapper;
import ptp.fltv.web.service.CommentService;

import java.util.*;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 6:41:50
 * @description 文章评论服务接口的实现类
 * @filename CommentServiceImpl.java
 */

@AllArgsConstructor
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {


    private StringRedisTemplate stringRedisTemplate;


    @Override
    public List<Comment> getRankListByPage(@Nonnull ContentRankType contentRankType, @Nonnull Long offset, @Nonnull Long count) {

        String key = String.format("content:passage:comment:%s:total", contentRankType.getSubkey());
        Set<String> idsSet = stringRedisTemplate.opsForZSet().reverseRange(key, offset, offset + count - 1);

        if (idsSet == null || idsSet.isEmpty()) {

            return Collections.emptyList();

        }

        // 2024-6-21  21:27-由于BaseMapper::selectBatchIds方法获取到的实体数据会默认按其主键升序排列 , 而我们需要保持数据在排行榜中的数据 , 因此还需要进行一些后处理
        List<Comment> comments = baseMapper.selectBatchIds(idsSet);
        Map<Long, Comment> tempMap = new HashMap<>();

        for (Comment comment : comments) {

            tempMap.put(comment.getId(), comment);

        }

        comments.clear();
        for (String idStr : idsSet) {

            comments.add(tempMap.get(Long.parseLong(idStr)));

        }

        return comments;

    }


    @Override
    public List<Comment> queryCommentPageWithSorting(@Nonnull ContentQuerySortType sortType, @Nonnull Comment.BelongType belongType, @Nonnull Long contentId, @Nonnull Long uid, @Nonnull Long pageNum, @Nonnull Long pageSize) {

        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();

        if (belongType != Comment.BelongType.ALL) {

            queryWrapper.eq("belong_type", belongType.getCode());

        }

        if (contentId != -1) {

            queryWrapper.eq("content_id", contentId);

        }

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
            case OWNER -> queryWrapper.eq("uid", uid); // 2024-10-14  20:47-拥有者排序类型下将只筛选出内容发布者的评论
            case DEFAULT -> {


            }

        }

        // 2024-10-2  1:33-最后兜底的参与排序的字段
        queryWrapper.orderByDesc("id");

        List<Comment> comments = page(new Page<>(pageNum, pageSize), queryWrapper).getRecords();

        return comments == null ? new ArrayList<>() : comments;

    }


}
