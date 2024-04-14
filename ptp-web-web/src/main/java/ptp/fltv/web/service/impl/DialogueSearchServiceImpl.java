package ptp.fltv.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import pfp.fltv.common.model.po.content.Dialogue;
import pfp.fltv.common.model.vo.ContentSearchVo;
import ptp.fltv.web.service.ContentSearchService;

import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/14 下午 8:10:50
 * @description 内容搜索服务的对话搜索服务实现
 * @filename DialogueSearchServiceImpl.java
 */

@Service
public class DialogueSearchServiceImpl implements ContentSearchService<Dialogue> {


    @Autowired
    private ElasticsearchOperations elasticsearchOperations;


    @Override
    public List<Dialogue> searchContentPage(ContentSearchVo contentSearchVo) {

        elasticsearchOperations.indexOps(Dialogue.class);
        return List.of();

    }


}
