package ptp.fltv.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.annotation.Nonnull;
import pfp.fltv.common.enums.ContentRankType;
import pfp.fltv.common.model.po.content.Passage;

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


}
