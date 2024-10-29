package ptp.fltv.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pfp.fltv.common.exceptions.PtpException;
import pfp.fltv.common.model.po.content.*;
import pfp.fltv.common.model.po.finance.Commodity;
import pfp.fltv.common.model.po.manage.User;
import pfp.fltv.common.model.po.system.EventRecord;
import ptp.fltv.web.mapper.EventRecordMapper;
import ptp.fltv.web.service.*;

import java.lang.reflect.Field;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/10/29 AM 2:03:51
 * @description 事件记录服务接口的实现类
 * @filename EventRecordServiceImpl.java
 */

@RequiredArgsConstructor
@Service
public class EventRecordServiceImpl extends ServiceImpl<EventRecordMapper, EventRecord> implements EventRecordService {


    private final AnnouncementService announcementService;
    private final DialogueService dialogueService;
    private final PassageService passageService;
    private final CommentService commentService;
    private final BannerService bannerService;
    private final UserService userService;
    private final CommodityService commodityService;


    @Override
    public boolean insertSingleContentEventRecord(@Nonnull EventRecord eventRecord) {

        switch (eventRecord.getEventType()) {

            case LIKE, STAR, SUBSCRIBE -> {

                // 2024-10-30  00:15-对于这种正向事件(浏览、分享、评论事件除外)需要判断之前是否已经执行过(单个用户在单个内容类型的单个内容实体上)
                QueryWrapper<EventRecord> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("event_type", eventRecord.getEventType().getCode())
                        .eq("content_type", eventRecord.getContentType().getCode())
                        .eq("content_id", eventRecord.getContentId())
                        .eq("uid", eventRecord.getUid());
                long count = count(queryWrapper);

                if (count > 0) {

                    // 2024-10-30  00:18-则证明之前已经对单个内容类型的单个内容实体执行了相同的操作 , 因此此次操作直接失败
                    throw new PtpException(817, "重复操作!");

                }

            }

        }

        boolean isSaved = save(eventRecord);

        if (isSaved) {

            return handleContentEvent(eventRecord.getEventType(), eventRecord.getContentType(), eventRecord.getContentId(), eventRecord.getUid());

        } else {

            return false;

        }

    }


    @Override
    public boolean deleteSingleContentEventRecord(@Nonnull EventRecord.EventType eventType, @Nonnull Comment.BelongType contentType, @Nonnull Long contentId, @Nonnull Long uid) {

        QueryWrapper<EventRecord> queryWrapper = new QueryWrapper<>();
        // 2024-10-30  00:42-我们规定 , event_record表中只保存正向事件记录 , 因此这里在执行相应的反向事件操作时 , 其实就是需要清除其对应的正向事件 , 而对应的正向事件枚举代码为其反向事件的枚举代码-1(可以跳转至pfp.fltv.common.model.po.system.EventRecord.EventType查阅枚举代码参数)
        queryWrapper.eq("event_type", eventType.getCode() - 1)
                .eq("content_type", contentType.getCode())
                .eq("content_id", contentId)
                .eq("uid", uid);
        boolean isRemoved = remove(queryWrapper);

        if (isRemoved) {

            return handleContentEvent(eventType, contentType, contentId, uid);

        } else {

            return false;

        }

    }


    /**
     * @param eventType   内容事件的类型
     * @param contentType 内容实体的类型
     * @param contentId   内容实体的ID
     * @param uid         内容事件的发出者ID
     * @return 事件是否处理完毕
     * @author Lenovo/LiGuanda
     * @date 2024/10/29 PM 7:49:11
     * @version 1.0.0
     * @description 专门用于处理内容相关的事件的方法
     * @filename EventRecordServiceImpl.java
     */
    private boolean handleContentEvent(@Nonnull EventRecord.EventType eventType, @Nonnull Comment.BelongType contentType, @Nonnull Long contentId, @Nonnull Long uid) {

        int delta = switch (eventType) {

            case BROWSE, LIKE, STAR, COMMENT, SHARE, SUBSCRIBE -> 1;
            case CANCEL_BROWSE, CANCEL_LIKE, CANCEL_STAR, CANCEL_COMMENT, CANCEL_SHARE, CANCEL_SUBSCRIBE -> -1;
            default -> 0;

        };

        String fieldName = switch (eventType) {

            case BROWSE, CANCEL_BROWSE -> "browseNum";
            case LIKE, CANCEL_LIKE -> "likeNum";
            case STAR, CANCEL_STAR -> "starNum";
            case SHARE, CANCEL_SHARE -> "shareNum";
            case UNLIKE, CANCEL_UNLIKE -> "unlikeNum";
            case COMMENT, CANCEL_COMMENT -> "commentNum";
            default -> null;

        };

        switch (contentType) {

            case ANNOUNCEMENT -> {

                Announcement announcement = announcementService.getById(contentId);
                if (announcement != null) {

                    modifyNum(announcement, fieldName, delta);
                    return announcementService.updateById(announcement);

                }

            }

            case DIALOGUE -> {

                Dialogue dialogue = dialogueService.getById(contentId);
                if (dialogue != null) {

                    modifyNum(dialogue, fieldName, delta);
                    return dialogueService.updateById(dialogue);

                }

            }

            case PASSAGE -> {

                Passage passage = passageService.getById(contentId);
                if (passage != null) {

                    modifyNum(passage, fieldName, delta);
                    return passageService.updateById(passage);

                }

            }

            case COMMENT -> {

                Comment comment = commentService.getById(contentId);
                if (comment != null) {

                    modifyNum(comment, fieldName, delta);
                    return commentService.updateById(comment);

                }

            }

            case BANNER -> {

                Banner banner = bannerService.getById(contentId);
                if (banner != null) {

                    modifyNum(banner, fieldName, delta);
                    return bannerService.updateById(banner);

                }

            }

            case COMMODITY -> {

                Commodity commodity = commodityService.getById(contentId);
                if (commodity != null) {

                    modifyNum(commodity, fieldName, delta);
                    return commodityService.updateById(commodity);

                }

            }

            case USER -> {

                User user = userService.getById(contentId);
                if (user != null) {

                    modifyNum(user, fieldName, delta);
                    return userService.updateById(user);

                }

            }

        }

        return false;

    }


    /**
     * @param obj       需要改变字段数值的对象
     * @param fieldName 数值字段的字段名
     * @param delta     数值的变化量(正数则为增加 , 负数则为减少)
     * @author Lenovo/LiGuanda
     * @date 2024/10/29 PM 8:25:31
     * @version 1.0.0
     * @apiNote 注意 : 该方法的实现用到了反射 , 执行效率会受到影响
     * @description 调整指定对象的指定字段的数值(通过提供值的增量 ( 增量可以为负值))
     * @filename EventRecordServiceImpl.java
     */
    private void modifyNum(Object obj, String fieldName, Integer delta) {

        try {

            Class<?> clazz = obj.getClass();
            Field field;
            if (clazz.getSuperclass() != null) {

                field = clazz.getSuperclass().getDeclaredField(fieldName);

            } else {

                field = clazz.getDeclaredField(fieldName);

            }

            field.setAccessible(true);
            Object fieldVal = field.get(obj);
            if (fieldVal instanceof Integer fieldValue) {

                if (fieldValue < 0) {

                    fieldValue = 0;

                }

                field.set(obj, fieldValue + delta);

            }

        } catch (Exception ignore) {

            // 2024-10-29  20:39-忽略该异常并不会产生什么恶劣的影响 , 大不了就是因为找不到指定字段而抛出字段不存在的异常从而导致对象的指定字段的数值没有变化罢了

        }

    }


}