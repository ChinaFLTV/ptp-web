package ptp.fltv.web.service.elasticsearch.service;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.elasticsearch.core.query.ByQueryResponse;
import org.springframework.data.elasticsearch.core.query.UpdateResponse;

import java.util.List;
import java.util.Map;

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
     * @param clazz    查询的实体类类型
     * @param <D>      要查询的实体类型
     * @return 查询到的符合条件的内容实体页
     * @author Lenovo/LiGuanda
     * @date 2024/4/18 PM 9:05:18
     * @version 1.0.0
     * @description 根据关键词组进行分页查询
     * @filename ContentIntermediateService.java
     */
    <D> List<D> pagingQueryByKeywords(@Nonnull List<String> keywords, @Nonnull String field, @Nonnull Long offset, @Nonnull Long limit, @Nonnull Class<D> clazz);


    /**
     * @param entity  待添加的内容实体
     * @param options 其他数据配置(可选)
     * @param <T>     实体的真实类型
     * @return 成功插入的内容实体
     * @author Lenovo/LiGuanda
     * @date 2024/4/29 AM 9:59:24
     * @version 1.0.0
     * @description 添加一条实体数据
     * @filename EsSearchService.java
     */
    <T> T insertEntity(@Nonnull T entity, @Nullable Map<String, Object> options);


    /**
     * @param entity  待更新的内容实体(需包含内容ID)
     * @param options 其他数据配置(可选)
     * @param <T>     实体的真实类型
     * @return 更新完成后返回的响应
     * @author Lenovo/LiGuanda
     * @date 2024/4/29 AM 10:44:50
     * @version 1.0.0
     * @description 更新一条实体数据
     * @filename EsSearchService.java
     */
    <T> UpdateResponse updateEntity(@Nonnull T entity, @Nullable Map<String, Object> options);


    /**
     * @param id      待删除实体的ID
     * @param clazz   实体所属的类类型
     * @param options 其他数据配置(可选)
     * @param <T>     实体的真实类型
     * @return 删除完成后返回的响应数据
     * @author Lenovo/LiGuanda
     * @date 2024/4/29 AM 10:49:48
     * @version 1.0.0
     * @description 根据ID删除一条实体数据
     * @filename EsSearchService.java
     */
    <T> ByQueryResponse deleteEntityById(@Nonnull Long id, @Nonnull Class<T> clazz, @Nullable Map<String, Object> options);


}
