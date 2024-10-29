package ptp.fltv.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import pfp.fltv.common.exceptions.PtpException;
import pfp.fltv.common.model.po.manage.SubscriberShip;
import ptp.fltv.web.mapper.SubscriberShipMapper;
import ptp.fltv.web.service.SubscriberShipService;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/10/21 PM 6:14:56
 * @description 订阅关系服务接口的实现类
 * @filename SubscriberShipServiceImpl.java
 */

@Service
public class SubscriberShipServiceImpl extends ServiceImpl<SubscriberShipMapper, SubscriberShip> implements SubscriberShipService {


    @Override
    public SubscriberShip querySingleSubscription(@Nonnull Long followerId, @Nonnull Long followeeId) {

        QueryWrapper<SubscriberShip> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("follower_id", followerId)
                .eq("followee_id", followeeId);

        return getOne(queryWrapper);

    }


    @Override
    public long insertSingleSubscription(@Nonnull Long followerId, @Nonnull Long followeeId) {

        QueryWrapper<SubscriberShip> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("follower_id", followerId)
                .eq("followee_id", followeeId);

        // 2024-10-30  2:06-如果用户先前已经订阅过同一个用户 , 则直接以抛出业务异常的形式终止本次订阅操作
        long count = count(queryWrapper);
        if (count > 0) {

            throw new PtpException(817, "重复订阅!");

        }

        SubscriberShip subscriberShip = SubscriberShip.builder()
                .uid(followerId)
                .followerId(followerId)
                .followeeId(followeeId)
                .build();

        boolean isSaved = save(subscriberShip);

        if (isSaved) {

            return subscriberShip.getId();

        } else {

            return -1;

        }

    }


    @Override
    public boolean deleteSingleSubscription(@Nonnull Long followerId, @Nonnull Long followeeId) {

        QueryWrapper<SubscriberShip> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("follower_id", followerId)
                .eq("followee_id", followeeId);

        return remove(queryWrapper);

    }


}