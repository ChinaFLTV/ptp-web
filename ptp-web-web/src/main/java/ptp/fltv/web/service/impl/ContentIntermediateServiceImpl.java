package ptp.fltv.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;
import ptp.fltv.web.service.EsSearchService;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/18 PM 9:02:07
 * @description 所有内容实体的中间服务实现
 * @filename ContentIntermediateServiceImpl.java
 */

@Service
public class ContentIntermediateServiceImpl implements EsSearchService {


    @Autowired
    private ElasticsearchOperations elasticsearchOperations;


    @Override
    public <D> List<D> pagingQueryByKeywords(List<String> keywords, String field, Long offset, Long limit) {

        List<D> ds = new ArrayList<>();

        if (keywords != null && !keywords.isEmpty()) {

            Criteria criteria = new Criteria(field).contains(keywords.get(0));

            for (int i = 1; i < keywords.size(); i++) {

                criteria.and(field)
                        .contains(keywords.get(i));

            }

            // 2024-4-18  22:17-之前强行获取泛型的真实类型的遗址~
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

            }


        }

        return ds;

    }


}
