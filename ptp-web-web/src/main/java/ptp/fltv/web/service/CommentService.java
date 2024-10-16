package ptp.fltv.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.annotation.Nonnull;
import pfp.fltv.common.enums.ContentQuerySortType;
import pfp.fltv.common.enums.ContentRankType;
import pfp.fltv.common.model.po.content.Comment;

import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 6:41:11
 * @description 文章评论服务接口
 * @filename PassageCommentService.java
 */

public interface CommentService extends IService<Comment> {


    /**
     * @param contentRankType 文章评论实体的排行类型
     * @param offset          获取的文章评论排行榜条目在排行榜的起始偏移量
     * @param count           需要获取的文章评论排行榜条目数量
     * @return 排行榜的文章评论数据条目集合
     * @author Lenovo/LiGuanda
     * @date 2024/6/21 PM 8:28:07
     * @version 1.0.0
     * @implNote 注意:作为后端系统，我们不提供返回全部排行榜的数据API，这种操作完全可以由客户端一侧通过调用现有API自行封装的
     * @description 分页获取指定文章评论实体类型的指定排行类型的数据条目集合
     * @filename PassageCommentService.java
     */
    List<Comment> getRankListByPage(@Nonnull ContentRankType contentRankType, @Nonnull Long offset, @Nonnull Long count);


    /**
     * @param sortType   排序规则
     * @param belongType 评论归属的实体类型
     * @param contentId  评论归属的实体ID(值为-1则该参数失效)
     * @param uid        内容发布者的ID(非必需)(仅在排序类型为拥有者类型下生效)
     * @param pageNum    页码(从1开始)
     * @param pageSize   数据页大小
     * @return 查询到的符合条件的指定数据页
     * @author Lenovo/LiGuanda
     * @date 2024/10/13 AM 12:41:17
     * @version 1.0.0
     * @description 根据指定的排序规则分页查询指定页码、指定大小的数据页
     * @filename CommentService.java
     */
    List<Comment> queryCommentPageWithSorting(@Nonnull ContentQuerySortType sortType, @Nonnull Comment.BelongType belongType, @Nonnull Long contentId, @Nonnull Long uid, @Nonnull Long pageNum, @Nonnull Long pageSize);


    /**
     * @param id 内容评论ID
     * @author Lenovo/LiGuanda
     * @date 2024/10/17 AM 1:39:53
     * @version 1.0.0
     * @apiNote 这里之所以又自定义了删除评论的逻辑 , 是因为删除评论的同时可能还需要给可能存在的父级评论的评论数-1
     * @description 根据评论ID(逻辑)删除单条内容评论
     * @filename CommentService.java
     */
    boolean deleteSingleComment(@Nonnull Long id);


}
