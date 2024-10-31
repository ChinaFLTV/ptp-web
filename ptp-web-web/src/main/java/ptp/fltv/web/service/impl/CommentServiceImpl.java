package ptp.fltv.web.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import pfp.fltv.common.enums.ContentQuerySortType;
import pfp.fltv.common.enums.ContentRankType;
import pfp.fltv.common.model.po.content.Comment;
import pfp.fltv.common.model.po.manage.Rate;
import pfp.fltv.common.model.po.system.EventRecord;
import pfp.fltv.common.model.vo.CommentVo;
import ptp.fltv.web.mapper.CommentMapper;
import ptp.fltv.web.service.CommentService;
import ptp.fltv.web.service.EventRecordService;
import ptp.fltv.web.service.RateService;

import java.util.*;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 6:41:50
 * @description 文章评论服务接口的实现类
 * @filename CommentServiceImpl.java
 */

@RequiredArgsConstructor
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {


    private final StringRedisTemplate stringRedisTemplate;
    private final RateService rateService;


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
            case OWNER -> queryWrapper.eq("from_uid", uid); // 2024-10-14  20:47-拥有者排序类型下将只筛选出内容发布者的评论
            case DEFAULT -> {


            }

        }

        // 2024-10-2  1:33-最后兜底的参与排序的字段
        queryWrapper.orderByDesc("id");

        List<Comment> comments = page(new Page<>(pageNum, pageSize), queryWrapper).getRecords();

        return comments == null ? new ArrayList<>() : comments;

    }


    @Override
    public boolean deleteSingleComment(@Nonnull Long id) {

        boolean isRemoved = false;

        Comment comment = getById(id);

        if (comment != null) {

            isRemoved = removeById(id);
            // 2024-10-17  1:42-如果当前删除的评论为二级评论，则还需要给其父级评论的评论数-1
            if (isRemoved && comment.getParentId() != null && comment.getParentId() > 0) {

                Comment parentComment = getById(comment.getParentId());
                if (parentComment != null) {

                    parentComment.setCommentNum(parentComment.getCommentNum() - 1);
                    if (parentComment.getCommentNum() < 0) {

                        parentComment.setCommentNum(0);

                    }

                    updateById(parentComment);

                }

            }

        }

        return isRemoved;

    }


    @Override
    public List<CommentVo> queryCommentVoPage(@Nonnull Long offset, @Nonnull Long limit) {

        Page<Comment> commentPage = new Page<>(offset, limit);
        commentPage = page(commentPage);

        List<CommentVo> commentVos = new ArrayList<>();

        if (commentPage != null && !commentPage.getRecords().isEmpty()) {

            for (Comment comment : commentPage.getRecords()) {

                CommentVo commentVo = new CommentVo();
                BeanUtils.copyProperties(comment, commentVo);

                QueryWrapper<Rate> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("content_id", comment.getContentId());
                queryWrapper.eq("uid", comment.getFromUid());
                Rate userRate = rateService.getOne(queryWrapper);

                commentVo.setAverageScore(-1D); // 2024-10-27  19:37-这里设置-1是为了便于前端处理有些用户尚未进行评分时的评论展示的情况 , 客户端可根据该字段是否为-1来确认当前用户有没有评分过
                if (userRate != null) {

                    commentVo.setContentTags(userRate.getContentTags());

                    double userAverageScore = userRate.getRateMap()
                            .values()
                            .stream()
                            .mapToDouble(Double::doubleValue)
                            .average()
                            .orElse(-1D);

                    commentVo.setAverageScore(userAverageScore);

                }

                commentVos.add(commentVo);

            }

        }

        return commentVos;

    }


    @Override
    public List<CommentVo> queryCommentVoPageWithSorting(@Nonnull ContentQuerySortType sortType, @Nonnull Comment.BelongType belongType, @Nonnull Long contentId, @Nonnull Long uid, @Nonnull Long pageNum, @Nonnull Long pageSize, @Nonnull Long requestUserId) {

        List<Comment> comments = queryCommentPageWithSorting(sortType, belongType, contentId, uid, pageNum, pageSize);

        List<CommentVo> commentVos = new ArrayList<>();
        for (Comment comment : comments) {

            CommentVo commentVo = new CommentVo();
            BeanUtils.copyProperties(comment, commentVo);

            QueryWrapper<Rate> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("content_id", comment.getContentId());
            queryWrapper.eq("uid", comment.getFromUid());
            Rate userRate = rateService.getOne(queryWrapper);

            commentVo.setAverageScore(-1D); // 2024-10-27  20:51-这里设置-1是为了便于前端处理有些用户尚未进行评分时的评论展示的情况 , 客户端可根据该字段是否为-1来确认当前用户有没有评分过
            if (userRate != null) {

                commentVo.setContentTags(userRate.getContentTags());

                double userAverageScore = userRate.getRateMap()
                        .values()
                        .stream()
                        .mapToDouble(Double::doubleValue)
                        .average()
                        .orElse(-1D);

                commentVo.setAverageScore(userAverageScore);

            }

            // 2024-10-31  2:27-这里之所以没有直接注入EventRecordService然后使用 , 是因为这样做可能会导致循环依赖
            // 2024-10-31  00:55-这里还需要额外统计一下当前评论是否被当前请求用户点赞过 , 以向用户在展示评论的同时展示评论点赞与否的状态
            EventRecord eventRecord = SpringUtil.getBean(EventRecordService.class).querySingleContentEventRecord(EventRecord.EventType.LIKE, Comment.BelongType.COMMENT, comment.getId(), requestUserId);
            commentVo.setIsLiked(eventRecord != null);

            commentVos.add(commentVo);

        }

        return commentVos;

    }


}
