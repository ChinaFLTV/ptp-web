package ptp.fltv.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.annotation.Nonnull;
import pfp.fltv.common.enums.ContentRankType;
import pfp.fltv.common.model.po.content.Announcement;

import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 6:07:49
 * @description 公告服务接口
 * @filename AnnouncementService.java
 */

public interface AnnouncementService extends IService<Announcement> {


    /**
     * @param contentRankType 公告实体的排行类型
     * @param offset          获取的公告排行榜条目在排行榜的起始偏移量
     * @param count           需要获取的公告排行榜条目数量
     * @return 排行榜的公告数据条目集合
     * @author Lenovo/LiGuanda
     * @date 2024/6/21 PM 8:16:32
     * @version 1.0.0
     * @implNote 注意:作为后端系统，我们不提供返回全部排行榜的数据API，这种操作完全可以由客户端一侧通过调用现有API自行封装的
     * @description 分页获取指定公告实体类型的指定排行类型的数据条目集合
     * @filename AnnouncementService.java
     */
    List<Announcement> getRankListByPage(@Nonnull ContentRankType contentRankType, @Nonnull Long offset, @Nonnull Long count);


}
