package ptp.fltv.web.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
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
import pfp.fltv.common.model.po.content.Announcement;
import pfp.fltv.common.model.po.content.Comment;
import pfp.fltv.common.model.po.content.Dialogue;
import pfp.fltv.common.model.po.content.Passage;
import pfp.fltv.common.model.po.manage.Asset;
import pfp.fltv.common.model.po.manage.Rate;
import pfp.fltv.common.model.po.manage.User;
import pfp.fltv.common.model.po.system.EventRecord;
import pfp.fltv.common.model.vo.CommentVo;
import ptp.fltv.web.mapper.CommentMapper;
import ptp.fltv.web.service.*;

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


    @Override
    public boolean insertSingleComment(@Nonnull Comment comment) {

        boolean isSaved = save(comment);
        if (isSaved) {

            // 2024-11-2  23:17-如果当前删除的评论为二级评论，则还需要给其父级评论的评论数+1
            if (comment.getParentId() != null && comment.getParentId() > 0) {

                updateContentCommentNum(EventRecord.EventType.COMMENT, Comment.BelongType.COMMENT, comment.getParentId());

            } else {

                // 2024-11-2  23:18-其他类型的内容实体的更新则交由这里进行处理
                updateContentCommentNum(EventRecord.EventType.COMMENT, comment.getBelongType(), comment.getContentId());

            }

            // 2024-11-11  19:35-添加评论成功后自动给发布者用户增加0.05积分(该积分增加操作失败也不会告知用户 , 毕竟这几乎不会产生副作用(可能就用户自己少赚得一点积分...))
            EventRecordService eventRecordService = SpringUtil.getBean(EventRecordService.class);
            AssetService assetService = SpringUtil.getBean(AssetService.class);
            UserService userService = SpringUtil.getBean(UserService.class);

            User user = userService.getById(comment.getFromUid());
            if (user != null) {

                EventRecord eventRecord = EventRecord.builder()
                        .uid(user.getId())
                        .nickname(user.getNickname())
                        .avatarUrl(JSON.parseObject(user.getAvatar()).getString("uri"))
                        .contentType(Comment.BelongType.ASSET)
                        .contentId(user.getAssetId())
                        .eventType(EventRecord.EventType.EARN)
                        .remark("因 发布一条评论(id = %d) 而 获得 0.05 积分".formatted(comment.getId()))
                        .build();

                boolean isSavedEventRecord = eventRecordService.save(eventRecord);
                if (isSavedEventRecord) {

                    Asset asset = assetService.getById(user.getAssetId());
                    if (asset != null) {

                        asset.setBalance(asset.getBalance() + 0.05);
                        assetService.updateById(asset);

                    }

                }

            }

        }

        return isSaved;

    }


    @Override
    public boolean deleteSingleComment(@Nonnull Long id) {

        boolean isRemoved = false;

        Comment comment = getById(id);

        if (comment != null) {

            isRemoved = removeById(id);
            if (isRemoved) {

                // 2024-10-17  1:42-如果当前删除的评论为二级评论，则还需要给其父级评论的评论数-1
                if (comment.getParentId() != null && comment.getParentId() > 0) {

                    updateContentCommentNum(EventRecord.EventType.CANCEL_COMMENT, Comment.BelongType.COMMENT, comment.getParentId());

                } else {

                    // 2024-11-2  23:11-其他类型的内容实体的更新则交由这里进行处理
                    updateContentCommentNum(EventRecord.EventType.CANCEL_COMMENT, comment.getBelongType(), comment.getContentId());

                }

            }

        }

        return isRemoved;

    }


    /**
     * @param eventType   内容实体评论量的事件类型
     * @param contentType 内容实体的类型
     * @param contentId   内容实体的ID
     * @apiNote 注意 : 即使本次内容实体的评论量数据更新失败 , 也不会抛出异常 , 因此一次操作的失败并不会导致致命的错误(有时候 , 忽略它比解决它更具价值)
     * @author Lenovo/LiGuanda
     * @date 2024/11/2 PM 10:41:39
     * @version 1.0.0
     * @description 同步更新评论所附属的内容实体的评论量数据
     * @filename CommentServiceImpl.java
     */
    private void updateContentCommentNum(@Nonnull EventRecord.EventType eventType, @Nonnull Comment.BelongType contentType, @Nonnull Long contentId) {

        int delta = switch (eventType) {

            case COMMENT -> 1;
            case CANCEL_COMMENT -> -1;
            default -> 0;

        };

        switch (contentType) {

            case ANNOUNCEMENT -> {

                AnnouncementService announcementService = SpringUtil.getBean(AnnouncementService.class);
                Announcement announcement = announcementService.getById(contentId);
                if (announcement != null) {

                    announcement.setCommentNum(announcement.getCommentNum() + delta);
                    if (announcement.getCommentNum() < 0) {

                        announcement.setCommentNum(0);

                    }
                    announcementService.updateById(announcement);

                }

            }

            case DIALOGUE -> {

                DialogueService dialogueService = SpringUtil.getBean(DialogueService.class);
                Dialogue dialogue = dialogueService.getById(contentId);
                if (dialogue != null) {

                    dialogue.setCommentNum(dialogue.getCommentNum() + delta);
                    if (dialogue.getCommentNum() < 0) {

                        dialogue.setCommentNum(0);

                    }
                    dialogueService.updateById(dialogue);

                }

            }

            case PASSAGE -> {

                PassageService passageService = SpringUtil.getBean(PassageService.class);
                Passage passage = passageService.getById(contentId);
                if (passage != null) {

                    passage.setCommentNum(passage.getCommentNum() + delta);
                    if (passage.getCommentNum() < 0) {

                        passage.setCommentNum(0);

                    }
                    passageService.updateById(passage);

                }

            }

            case COMMENT -> {

                Comment comment = getById(contentId);
                if (comment != null) {

                    comment.setCommentNum(comment.getCommentNum() + delta);
                    if (comment.getCommentNum() < 0) {

                        comment.setCommentNum(0);

                    }
                    updateById(comment);

                }

            }

        }

    }


}
