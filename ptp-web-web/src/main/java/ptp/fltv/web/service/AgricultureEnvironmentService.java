package ptp.fltv.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.annotation.Nonnull;
import pfp.fltv.common.model.po.agriculture.AgricultureEnvironment;

import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/11/29 PM 8:52:03
 * @description 农业生产环境数据服务接口
 * @filename AgricultureEnvironmentService.java
 */

public interface AgricultureEnvironmentService extends IService<AgricultureEnvironment> {


    /**
     * @param nodeId 需要查询最新一条农业生产环境数据的节点ID
     * @return 指定节点的最新一条农业生产环境数据
     * @author Lenovo/LiGuanda
     * @date 2024/11/29 PM 9:40:56
     * @version 1.0.0
     * @description 根据节点ID查询该节点的最新一条单条农业生产环境数据
     * @filename AgricultureEnvironmentService.java
     */
    AgricultureEnvironment queryLatestSingleAgricultureEnvironmentByNodeId(@Nonnull Long nodeId);


    /**
     * @param nodeId   分组查询的一页农业生产环境数据的数据页页码
     * @param sortType 分组查询的一页农业生产环境数据的排序规则
     * @param pageNum  查询的一页农业生产环境数据的数据页页码
     * @param pageSize 查询的这一页农业生产环境数据的数量
     * @return 查询到的符合条件按照指定排序规则排布的指定数据页
     * @author Lenovo/LiGuanda
     * @date 2024/11/29 PM 10:15:24
     * @version 1.0.0
     * @description 批量根据指定排序规则(分页)查询多条农业生产环境数据(对实体可见状态不做限制)
     * @filename AgricultureEnvironmentService.java
     */
    List<AgricultureEnvironment> queryAgricultureEnvironmentPageByNodeIdWithSorting(@Nonnull Long nodeId, @Nonnull AgricultureEnvironment.SortType sortType, @Nonnull Long pageNum, @Nonnull Long pageSize);


}