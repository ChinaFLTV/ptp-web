package ptp.fltv.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.annotation.Nonnull;
import pfp.fltv.common.enums.ContentQuerySortType;
import pfp.fltv.common.enums.ContentRankType;
import pfp.fltv.common.model.po.content.Passage;
import pfp.fltv.common.model.po.system.EventRecord;

import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 5:05:37
 * @description 文章服务接口
 * @filename PassageService.java
 */

public interface PassageService extends IService<Passage> {


    /**
     * @param contentRankType 文章实体的排行类型
     * @param offset          获取的文章排行榜条目在排行榜的起始偏移量
     * @param count           需要获取的文章排行榜条目数量
     * @return 排行榜的文章数据条目集合
     * @date 2024/6/21 PM 8:26:37
     * @author Lenovo/LiGuanda
     * @version 1.0.0
     * @implNote 注意:作为后端系统，我们不提供返回全部排行榜的数据API，这种操作完全可以由客户端一侧通过调用现有API自行封装的
     * @description 分页获取指定文章实体类型的指定排行类型的数据条目集合
     * @filename PassageService.java
     */
    List<Passage> getRankListByPage(@Nonnull ContentRankType contentRankType, @Nonnull Long offset, @Nonnull Long count);


    /**
     * @param sortType 排序规则
     * @param uid      当前请求发起用户的ID(非必需)(仅在排序类型为订阅类型下生效)
     * @param pageNum  页码(从1开始)
     * @param pageSize 数据页大小
     * @return 查询到的符合条件的指定数据页
     * @author Lenovo/LiGuanda
     * @date 2024/10/21 PM 4:50:59
     * @version 1.0.0
     * @description 根据指定的排序规则分页查询指定页码、指定大小的数据页
     * @filename PassageService.java
     */
    List<Passage> queryPassagePageWithSorting(@Nonnull ContentQuerySortType sortType, @Nonnull Long pageNum, @Nonnull Long pageSize, @Nonnull Long uid);


    /**
     * @param uid       当前请求发起用户的ID(非必需)(仅在排序类型为订阅类型下生效)
     * @param offset    数据页的第一条记录的起始偏移量
     * @param limit     数据页的页大小
     * @param eventType 内容事件的类型
     * @return 符合条件的指定文章PO数据列表
     * @apiNote 该API返回的文章列表是按照文章点赞时间的倒序排序的(即列表中的第一个记录是最新点赞的文章)
     * @author Lenovo/LiGuanda
     * @date 2024/10/31 AM 1:16:44
     * @version 1.0.0
     * @description 批量(分页)查询指定用户采取过指定动作的多条文章数据
     * @filename PassageService.java
     */
    List<Passage> queryOperatedPassagePage(@Nonnull EventRecord.EventType eventType, @Nonnull Long uid, @Nonnull Long offset, @Nonnull Long limit);


    /**
     * @return 是否保存文章成功
     * @author Lenovo/LiGuanda
     * @date 2024/10/26 AM 1:41:47
     * @version 1.0.0
     * @apiNote 之所以需要重写保存单个文章的API , 是因为需要在保存文章之前先保存一条对应的文章评分统计记录 , 然后再同步给需要保存的文章中
     * @description 保存单个文章
     * @filename PassageService.java
     */
    boolean saveSinglePassage(@Nonnull Passage passage);


    /**
     * @return 是否删除文章成功
     * @author Lenovo/LiGuanda
     * @date 2024/10/26 AM 1:54:10
     * @version 1.0.0
     * @apiNote 之所以需要重写删除单个文章的API , 是因为需要在删除文章之后还要删除文章所关联的文章评分统计记录(即使删除不成功也无伤大雅 , 这仅会产生浮动垃圾 , 不会产生副作用 , 可以定期排查删除掉)
     * @description 删除单个文章
     * @filename PassageService.java
     */
    boolean deleteSinglePassage(@Nonnull Long id);


    /**
     * @param uid    文章所属的用户ID
     * @param offset 查询的一页文章数据的起始偏移量
     * @param limit  查询的这一页文章数据的数量
     * @return 指定用户发布的指定偏移量的指定大小的文章数据页
     * @author Lenovo/LiGuanda
     * @date 2024/10/28 PM 7:24:22
     * @version 1.0.0
     * @description 批量(分页)查询指定用户的多条文章数据
     * @filename PassageService.java
     */
    List<Passage> queryAvailablePassagePageByUid(@Nonnull Long uid, @Nonnull Long offset, @Nonnull Long limit);


}