package ptp.fltv.web.service;

import pfp.fltv.common.model.vo.ContentSearchVo;

import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/14 下午 8:10:23
 * @description 内容搜索服务
 * @filename ContentSearchService.java
 */

public interface ContentSearchService<T> {


    /**
     * @param contentSearchVo 用户提供的查询信息
     * @return 查询到的多条泛型实体数据
     * @author Lenovo/LiGuanda
     * @date 2024/4/14 下午 8:25:41
     * @version 1.0.0
     * @description 根据关键词查询多条泛型实体数据
     * @filename ContentSearchService.java
     */
    List<T> searchContentPage(ContentSearchVo contentSearchVo);


}
