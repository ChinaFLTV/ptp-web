package ptp.fltv.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.annotation.Nonnull;
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


}
