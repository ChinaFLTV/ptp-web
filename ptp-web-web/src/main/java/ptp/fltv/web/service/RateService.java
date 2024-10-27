package ptp.fltv.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.annotation.Nonnull;
import pfp.fltv.common.model.po.manage.Rate;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/10/26 AM 12:19:21
 * @description 评分服务接口
 * @filename RateService.java
 */

public interface RateService extends IService<Rate> {


    /**
     * @param contentId 内容实体ID
     * @param uid       评分用户的ID
     * @return 如果用户已经评分该内容实体了 , 则返回对应的评分记录 ; 若没有评分过 , 则返回null
     * @author Lenovo/LiGuanda
     * @date 2024/10/27 PM 4:54:25
     * @version 1.0.0
     * @description 根据内容ID和用户ID去查询用户在某个内容的评分记录数据
     * @filename RateService.java
     */
    Rate querySingleUserRate(@Nonnull Long contentId, @Nonnull Long uid);


    /**
     * @param rate 评分记录数据实体
     * @return 是否成功插入评分记录(( 该结果不包括同步更新好内容实体的评分统计记录的执行结果, 即可能出现插入成功了但实际上评分数据并没有作用到内容实体统计数据上))
     * @author Lenovo/LiGuanda
     * @date 2024/10/27 PM 5:13:42
     * @version 1.0.0
     * @apiNote 之所以要重写添加单条评分记录的逻辑 , 是因为我们需要针对评分记录类型做额外的不同的处理 , 比如针对用户评分记录 , 我们需要在添加好了该用户评分记录后还要同步更新对应的评论实体的评分统计记录
     * @apiNote 注意 : 前端传递过来的Rate数据包则的各种得分必须要提前等效转换为10分制
     * @description 添加单条评分数据
     * @filename RateService.java
     */
    boolean insertSingleRate(@Nonnull Rate rate);


}