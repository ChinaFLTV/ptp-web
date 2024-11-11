package ptp.fltv.web.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfp.fltv.common.enums.ContentQuerySortType;
import pfp.fltv.common.enums.ContentRankType;
import pfp.fltv.common.model.po.content.Comment;
import pfp.fltv.common.model.po.content.Dialogue;
import pfp.fltv.common.model.po.manage.Asset;
import pfp.fltv.common.model.po.manage.User;
import pfp.fltv.common.model.po.system.EventRecord;
import pfp.fltv.common.model.vo.DialogueVo;
import ptp.fltv.web.mapper.DialogueMapper;
import ptp.fltv.web.service.AssetService;
import ptp.fltv.web.service.DialogueService;
import ptp.fltv.web.service.EventRecordService;
import ptp.fltv.web.service.UserService;

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
    private UserService userService;


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


    @Override
    public List<DialogueVo> queryDialogueVoPageWithSorting(ContentQuerySortType sortType, Long pageNum, Long pageSize) {

        QueryWrapper<Dialogue> queryWrapper = new QueryWrapper<>();

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
            case DEFAULT -> {


            }

        }

        // 2024-10-2  1:33-最后兜底的参与排序的字段
        queryWrapper.orderByDesc("id");

        List<Dialogue> dialogues = page(new Page<>(pageNum, pageSize), queryWrapper).getRecords();

        List<DialogueVo> dialogueVos = new ArrayList<>();
        for (Dialogue dialogue : dialogues) {

            DialogueVo dialogueVo = new DialogueVo();
            BeanUtils.copyProperties(dialogue, dialogueVo);

            // 2024-10-2  15:37-填充发布者的昵称和头像URL信息
            User user = userService.getById(dialogueVo.getUid());
            dialogueVo.setNickname(user.getNickname());
            JSONObject avatarJSONObject = JSON.parseObject(user.getAvatar());
            dialogueVo.setAvatarUrl(avatarJSONObject.getString("uri"));

            dialogueVos.add(dialogueVo);

        }

        return dialogueVos;

    }


    @Override
    public DialogueVo querySingleDialogue(@Nonnull Long id) {

        DialogueVo dialogueVo = new DialogueVo();
        Dialogue dialogue = getById(id);

        if (dialogue != null) {

            BeanUtils.copyProperties(dialogue, dialogueVo);
            User user = userService.getById(dialogue.getUid());
            if (user != null) {

                dialogueVo.setNickname(user.getNickname());
                dialogueVo.setAvatarUrl(JSON.parseObject(user.getAvatar()).getString("uri"));

            }

        }

        return dialogueVo;

    }


    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean insertSingleDialogue(Dialogue dialogue) {

        boolean isSaved = save(dialogue);

        // 2024-11-11  16:21-添加言论成功后自动给发布者用户增加0.1积分(该积分增加操作失败也不会告知用户 , 毕竟这几乎不会产生副作用(可能就用户自己少赚得一点积分...))
        if (isSaved) {

            EventRecordService eventRecordService = SpringUtil.getBean(EventRecordService.class);
            AssetService assetService = SpringUtil.getBean(AssetService.class);

            User user = userService.getById(dialogue.getUid());
            if (user != null) {

                EventRecord eventRecord = EventRecord.builder()
                        .uid(user.getId())
                        .nickname(user.getNickname())
                        .avatarUrl(JSON.parseObject(user.getAvatar()).getString("uri"))
                        .contentType(Comment.BelongType.ASSET)
                        .contentId(user.getAssetId())
                        .eventType(EventRecord.EventType.EARN)
                        .remark("因 发布一条言论(id = %d) 而 获得 0.1 积分".formatted(dialogue.getId()))
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

        }

        return isSaved;

    }


}
