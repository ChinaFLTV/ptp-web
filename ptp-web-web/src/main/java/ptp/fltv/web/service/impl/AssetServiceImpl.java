package ptp.fltv.web.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfp.fltv.common.exceptions.PtpException;
import pfp.fltv.common.model.po.content.Comment;
import pfp.fltv.common.model.po.manage.Asset;
import pfp.fltv.common.model.po.manage.User;
import pfp.fltv.common.model.po.system.EventRecord;
import ptp.fltv.web.mapper.AssetMapper;
import ptp.fltv.web.service.AssetService;
import ptp.fltv.web.service.EventRecordService;
import ptp.fltv.web.service.UserService;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 7:01:25
 * @description 财产服务接口的实现类
 * @filename AssetServiceImpl.java
 */

@RequiredArgsConstructor
@Service
public class AssetServiceImpl extends ServiceImpl<AssetMapper, Asset> implements AssetService {


    private final EventRecordService eventRecordService;
    private final UserService userService;


    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean changeSingleAssetBalance(@Nonnull Long assetId, @Nonnull Long userId, @Nonnull EventRecord.EventType eventType, @Nonnull Double quantity, @Nonnull String remark) {

        Asset asset = getById(assetId);
        if (asset != null) {

            double changeQuantity = switch (eventType) {

                case EARN, PAY -> quantity;
                case CONSUME -> -quantity;
                default -> 0;

            };

            asset.setBalance(asset.getBalance() + changeQuantity);

            // 2024-11-9  19:53-解决用户过度取钱导致账户欠费的情况
            if (asset.getBalance() < 0) {

                throw new PtpException(808, "账户余额不足!");

            }

            boolean isUpdated = updateById(asset);
            if (isUpdated) {

                User user = userService.getById(userId);
                if (user != null) {

                    EventRecord eventRecord = EventRecord.builder()
                            .uid(userId)
                            .nickname(user.getNickname())
                            .avatarUrl(JSON.parseObject(user.getAvatar()).getString("uri"))
                            .contentType(Comment.BelongType.ASSET)
                            .contentId(assetId)
                            .eventType(eventType)
                            .remark(remark)
                            .build();

                    boolean isSaved = eventRecordService.save(eventRecord);
                    if (!isSaved) {

                        // 2024-11-9  17:49-由于我们采用的@Transactional注解 , 因此这里一旦部分业务失败将直接通过抛出异常使整个事务回滚 , 也省去了自己编写额外的if else语句去自行处理一致性的麻烦
                        throw new PtpException(808, "添加事件记录失败!");

                    }

                    return true;

                }

            }

        }

        return false;

    }


}