package ptp.fltv.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import pfp.fltv.common.model.po.content.Dialogue;
import ptp.fltv.web.service.DialogueService;

import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/14 下午 10:33:42
 * @description 检测并同时填充Mock数据到ElasticSearch中
 * @filename ElasticSearchDataOperationTest.java
 */

@SpringBootTest
public class ElasticSearchDataOperationTest {


    @Autowired
    private DialogueService dialogueService;
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;


    @Test
    public void fillData() {

        System.out.println("-----------------------------------------------------------------");
        List<Dialogue> dialogues = dialogueService.list();
        elasticsearchOperations.save(dialogues);

    }


}
