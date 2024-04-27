package ptp.fltv.web.service.elasticsearch.repository;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/16 PM 10:41:35
 * @description 定义内容实体对应的中间存储库，存储在ElasticSearch中进行的通用数据操作
 * @filename ElasticSearchPagingRepository.java
 */

@Deprecated
@NoRepositoryBean
public interface ElasticSearchPagingRepository<T> extends ListPagingAndSortingRepository<T, Long> {


    /**
     * @param pageable 分页信息
     * @return 查询到的实体页
     * @author Lenovo/LiGuanda
     * @date 2024/4/17 PM 10:01:27
     * @version 1.0.0
     * @description 在ElasticSearch中进行简单分页查询
     * @filename ElasticSearchPagingRepository.java
     */
    @Nonnull
    @Override
    Page<T> findAll(@Nonnull Pageable pageable);


    /**
     * @param pageable 分页信息
     * @author Lenovo/LiGuanda
     * @date 2024/4/17 PM 10:13:15
     * @version 1.0.0
     * @description 在ElasticSearch中根据给出的关键词组进行分页查询
     * @filename ElasticSearchPagingRepository.java
     */
    @Nonnull
    List<T> findByTitleLike(String title, Pageable pageable);


}
