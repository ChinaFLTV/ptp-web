package ptp.fltv.web.script;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchOperations;
import org.springframework.test.context.ContextConfiguration;
import pfp.fltv.common.model.po.content.Announcement;
import pfp.fltv.common.model.po.content.Dialogue;
import pfp.fltv.common.model.po.content.Passage;
import pfp.fltv.common.model.po.content.PassageComment;
import pfp.fltv.common.model.po.finance.Commodity;
import pfp.fltv.common.model.po.manage.User;
import ptp.fltv.web.WebApplication;
import ptp.fltv.web.service.*;

import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/15 PM 9:01:39
 * @description 用于将数据库中的对话、文章、文章评论、用户数据导入到ElasticSearch中(之所以没选择用Rest方式导入，是因为一方面Mock数据太多，另一方面这样也能保证MySQL数据库这边与ElasticSearch数据库这边的数据的一致)
 * @filename ElasticSearchDataFillScript.java
 */

@SuppressWarnings("NewClassNamingConvention")
@Slf4j(topic = "test_elasticsearch_filldata")
@SpringBootTest
@ContextConfiguration(classes = WebApplication.class)
public class ElasticSearchDataFillScript {


    @Autowired
    private AnnouncementService announcementService;
    @Autowired
    private DialogueService dialogueService;
    @Autowired
    private PassageService passageService;
    @Autowired
    private PassageCommentService passageCommentService;
    @Autowired
    private UserService userService;
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;
    @Autowired
    private SearchOperations searchOperations;
    @Autowired
    private CommodityService commodityService;


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/4/15 PM 9:38:15
     * @version 1.0.0
     * @description 向ElasticSearch中填充MySQL数据库对应的Mock数据
     * @filename ElasticSearchDataFillScript.java
     */
    @Test
    public void fillMockData() {

        try {

            flushDB();

            List<Announcement> announcements = announcementService.list();
            List<Dialogue> dialogues = dialogueService.list();
            List<Passage> passages = passageService.list();
            List<PassageComment> passageComments = passageCommentService.list();
            List<User> users = userService.list();
            List<Commodity> commodities = commodityService.list();

            elasticsearchOperations.save(announcements);
            elasticsearchOperations.save(dialogues);
            elasticsearchOperations.save(passages);
            elasticsearchOperations.save(passageComments);
            elasticsearchOperations.save(users);
            elasticsearchOperations.save(commodities);

        } catch (RuntimeException e) {

            log.error(e.getLocalizedMessage());
            log.error("向ElasticSearch中插入Mock数据失败!");
            flushDB();

        }

    }


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/4/15 PM 9:33:01
     * @version 1.0.0
     * @description 清除ElasticSearch中存储的全部数据
     * @filename ElasticSearchDataFillScript.java
     */
    private void flushDB() {

        elasticsearchOperations.indexOps(Announcement.class).delete();
        elasticsearchOperations.indexOps(Dialogue.class).delete();
        elasticsearchOperations.indexOps(Passage.class).delete();
        elasticsearchOperations.indexOps(PassageComment.class).delete();
        elasticsearchOperations.indexOps(User.class).delete();
        elasticsearchOperations.indexOps(Commodity.class).delete();

    }


}
