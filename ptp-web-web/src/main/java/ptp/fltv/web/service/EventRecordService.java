package ptp.fltv.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.annotation.Nonnull;
import pfp.fltv.common.model.po.content.Comment;
import pfp.fltv.common.model.po.system.EventRecord;

import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/10/29 AM 2:03:14
 * @description 事件记录服务接口
 * @filename EventRecordService.java
 */

public interface EventRecordService extends IService<EventRecord> {


    /**
     * @param eventType   内容事件的类型
     * @param contentType 内容事件所关联的内容实体的类型
     * @param contentId   内容事件所关联的内容实体的ID
     * @param uid         内容事件发出者ID
     * @return 查询到的符合条件的内容事件记录数据(不存在符合条件的内容记录数据则返回null)
     * @author Lenovo/LiGuanda
     * @date 2024/10/30 PM 4:28:05
     * @version 1.0.0
     * @description 查询单条内容事件记录数据
     * @filename EventRecordService.java
     */
    EventRecord querySingleContentEventRecord(@Nonnull EventRecord.EventType eventType, @Nonnull Comment.BelongType contentType, @Nonnull Long contentId, @Nonnull Long uid);


    /**
     * @param eventRecord 待插入的事件记录数据
     * @return 记录是否插入成功(暗含对应的额外的业务逻辑是否同步执行成功)
     * @author Lenovo/LiGuanda
     * @apiNote 建议正向操作(如浏览 、 点赞 、 收藏 、 订阅等操作)使用该API
     * @date 2024/10/29 PM 7:39:49
     * @version 1.0.0
     * @description 插入单条事件记录并执行对应的业务逻辑
     * @filename EventRecordService.java
     */
    boolean insertSingleContentEventRecord(@Nonnull EventRecord eventRecord);


    /**
     * @param contentType 内容浏览事件的目标内容实体的类型
     * @param contentId   内容浏览事件的目标内容实体的ID
     * @param uid         内容浏览事件的发出者ID
     * @return 如果内容浏览事件插入成功 , 则返回对应插入后的事件记录的ID , 否则 , 插入失败则返回-1
     * @author Lenovo/LiGuanda
     * @date 2024/10/31 PM 3:13:17
     * @version 1.0.0
     * @description 添加单条内容实体相关的浏览事件记录数据
     * @filename EventRecordService.java
     */
    Long insertSingleContentBrowseEventRecord(@Nonnull Comment.BelongType contentType, @Nonnull Long contentId, @Nonnull Long uid);


    /**
     * @param eventType   内容事件的类型
     * @param contentType 内容事件所关联的内容实体的类型
     * @param contentId   内容事件所关联的内容实体的ID
     * @param uid         内容事件发出者ID
     * @return 记录是否删除成功(暗含对应的额外的业务逻辑是否同步执行成功)
     * @author Lenovo/LiGuanda
     * @apiNote 建议反向操作(如取消点赞 、 取消收藏 、 取消订阅等操作)使用该API
     * @date 2024/10/29 PM 9:53:44
     * @version 1.0.0
     * @description 删除单条事件记录并执行对应的业务逻辑
     * @filename EventRecordService.java
     */
    boolean deleteSingleContentEventRecord(@Nonnull EventRecord.EventType eventType, @Nonnull Comment.BelongType contentType, @Nonnull Long contentId, @Nonnull Long uid);


    /**
     * @param eventType 财产事件的类型
     * @param assetId   财产事件的目标财产实体的ID
     * @param pageNum   查询的一页财产数据的数据页页码
     * @param pageSize  查询的这一页财产数据的数量
     * @return 查询到的符合条件的指定页数的指定页大小的财产记录列表(数据默认按照发布时间倒序排序)
     * @author Lenovo/LiGuanda
     * @date 2024/11/9 PM 8:26:12
     * @version 1.0.0
     * @description 根据事件分页查询财产事件记录数据
     * @filename EventRecordService.java
     */
    List<EventRecord> querySingleAssetEventRecordPage(EventRecord.EventType eventType, Long assetId, Long pageNum, Long pageSize);


}