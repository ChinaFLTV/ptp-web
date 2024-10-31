package ptp.fltv.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.annotation.Nonnull;
import pfp.fltv.common.model.po.manage.SubscriberShip;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/10/21 PM 6:14:15
 * @description 订阅关系服务接口
 * @filename SubscriberShipService.java
 */

public interface SubscriberShipService extends IService<SubscriberShip> {


    /**
     * @param followerId 订阅发起方的用户ID
     * @param followeeId 被订阅方的用户ID
     * @return 如果订阅操作双方存在订阅关系 , 则返回对应的订阅关系记录 , 否则 , 不存在则返回null
     * @author Lenovo/LiGuanda
     * @date 2024/10/30 AM 1:33:09
     * @version 1.0.0
     * @description 根据订阅操作的双方ID查询单条订阅关系数据
     * @filename SubscriberShipService.java
     */
    SubscriberShip querySingleSubscription(@Nonnull Long followerId, @Nonnull Long followeeId);


    /**
     * @param followerId 订阅发起方的用户ID
     * @param followeeId 被订阅方的用户ID
     * @return 如果订阅操作成功 , 则返回订阅关系记录的ID , 否则 , 订阅操作失败则返回-1
     * @author Lenovo/LiGuanda
     * @apiNote 注意 : 该API默认该订阅操作的发起者为订阅方
     * @date 2024/10/30 AM 1:53:40
     * @version 1.0.0
     * @description 根据订阅操作的双方ID添加单条订阅关系数据
     * @filename SubscriberShipService.java
     */
    long insertSingleSubscription(@Nonnull Long followerId, @Nonnull Long followeeId);


    /**
     * @param followerId 解除订阅发起方的用户ID
     * @param followeeId 被解除订阅方的用户ID
     * @return 解除订阅操作是否执行成功
     * @author Lenovo/LiGuanda
     * @date 2024/10/30 AM 1:43:07
     * @version 1.0.0
     * @description 根据解除订阅操作的双方ID删除单条订阅关系数据
     * @filename SubscriberShipService.java
     */
    boolean deleteSingleSubscription(@Nonnull Long followerId, @Nonnull Long followeeId);


    /**
     * @param followerId 订阅发起方的用户ID
     * @return 该订阅发起方订阅的全部订阅关系的总数
     * @author Lenovo/LiGuanda
     * @date 2024/10/31 PM 8:33:42
     * @version 1.0.0
     * @description 根据订阅发起方ID查询全部发起的订阅关系的总数
     * @filename SubscriberShipService.java
     */
    Long countAllSubscriptionsByFollowerId(@Nonnull Long followerId);


    /**
     * @param followeeId 被订阅方的用户ID
     * @return 该被订阅方被订阅的全部订阅关系的总数
     * @author Lenovo/LiGuanda
     * @date 2024/10/31 PM 8:41:33
     * @version 1.0.0
     * @description 根据被订阅方ID查询全部被发起的订阅关系的总数
     * @filename SubscriberShipService.java
     */
    Long countAllSubscriptionsByFolloweeId(@Nonnull Long followeeId);


}