package ptp.fltv.web.service.elasticsearch.service.impl;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.ByQueryResponse;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.UpdateResponse;
import org.springframework.stereotype.Service;
import ptp.fltv.web.service.elasticsearch.service.EsSearchService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/18 PM 9:02:07
 * @description 所有内容实体的中间服务实现
 * @filename ContentIntermediateServiceImpl.java
 */

@Service
public class EsSearchServiceImpl implements EsSearchService {


    @Autowired
    private ElasticsearchOperations elasticsearchOperations;


    @Override
    public <D> List<D> pagingQueryByKeywords(List<String> keywords, String field, Long offset, Long limit, Class<D> clazz) {

        List<D> ds = new ArrayList<>();

        if (keywords != null && !keywords.isEmpty()) {

            Criteria criteria = new Criteria(field).matchesAll(keywords);

            SearchHits<D> searchHits = elasticsearchOperations.search(new CriteriaQuery(criteria), clazz);
            for (SearchHit<D> searchHit : searchHits.getSearchHits()) {

                D content = searchHit.getContent();
                ds.add(content);

            }

            // 2024-4-18  22:17-之前强行获取泛型的真实类型的遗址~
            /*GenericTypeResolver.resolveTypeArgument(ds.getClass(), ArrayList.class);
            Type type = ds.getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType parameterizedType) {

                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (actualTypeArguments.length > 0) {

                    if (actualTypeArguments[0] instanceof Class<?> clazz) {

                        //noinspection unchecked
                        SearchHits<D> searchHits = (SearchHits<D>) elasticsearchOperations.search(new CriteriaQuery(criteria), clazz);
                        for (SearchHit<D> searchHit : searchHits.getSearchHits()) {

                            D content = searchHit.getContent();
                            ds.add(content);

                        }

                    }

                }

            }*/


        }

        return ds;

    }


    @Override
    public <T> T insertEntity(@Nonnull T entity, @Nullable Map<String, Object> options) {

        return elasticsearchOperations.save(entity);

    }


    @Override
    public <T> UpdateResponse updateEntity(@Nonnull T entity, @Nullable Map<String, Object> options) {

        return elasticsearchOperations.update(entity);

    }


    @Override
    public <T> ByQueryResponse deleteEntityById(@Nonnull Long id, @Nonnull Class<T> clazz, @Nullable Map<String, Object> options) {

        Criteria criteria = new Criteria("id").is(id);
        // TODO 还需要判断ElasticSearch这边是否成功执行了操作，否则还得回滚
        return elasticsearchOperations.delete(new CriteriaQuery(criteria), clazz);

    }


}
