package ptp.fltv.web.service;

import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/18 PM 9:04:04
 * @description 所有内容实体的中间服务
 * @filename ContentIntermediateService.java
 */


public interface EsSearchService {


    /**
     * @param keywords 用户提供的关键词组
     * @param field    要查询的字段
     * @param offset   查询到的内容的起始偏移量
     * @param limit    查询内容实体的数量
     * @param <D>      要查询的实体类型
     * @return 查询到的符合条件的内容实体页
     * @author Lenovo/LiGuanda
     * @date 2024/4/18 PM 9:05:18
     * @version 1.0.0
     * @description 根据关键词组进行分页查询
     * @filename ContentIntermediateService.java
     */
    <D> List<D> pagingQueryByKeywords(List<String> keywords, String field, Long offset, Long limit);


}
