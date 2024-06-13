package ptp.fltv.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.remoting.protocol.body.TopicList;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;
import org.apache.rocketmq.tools.command.CommandUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.support.MessageBuilder;

import java.util.Set;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/31 PM 11:03:55
 * @description 测试RocketMQ消息发送连通性
 * @filename RocketMQTest.java
 */

@Slf4j
@SpringBootTest
public class RocketMQTest {


    @Autowired
    private RocketMQTemplate rocketMQTemplate;


    @Test
    public void testSendMsg() {

        System.out.println("----------------------------------testSendMsg------START-------------------------");
        rocketMQTemplate.send("test-topic-1", MessageBuilder.withPayload("Hello, World! I'm from spring message").build());
        System.out.println("----------------------------------testSendMsg----END---------------------------");

    }


    @Test
    public void clearAccumulationalMsg() throws MQClientException, RemotingException, InterruptedException, MQBrokerException {

        DefaultMQAdminExt mqAdminExt = new DefaultMQAdminExt();
        mqAdminExt.setNamesrvAddr("10.224.4.192:9876");
        mqAdminExt.start();

        try {

            // 获取所有主题
            TopicList topicList = mqAdminExt.fetchAllTopicList();
            System.out.println("--------------------------------TopicList---------------------------------");
            for (String s : topicList.getTopicList()) {

                System.out.println("s = " + s);

            }
            // Set<String> topicSet = topicList.getTopicList();
            Set<String> topicSet = Set.of("%DLQ%commodity-seckill-consumer-group",
                    "%RETRY%commodity-replenish-consumer-group",
                    "%RETRY%commodity-seckill-consumer-group",
                    "%RETRY%commodity-seckill-record-consumer-group",
                    "commodity-replenish-topic",
                    "commodity-seckill-topic ");
            Set<String> brokerAddrSet = CommandUtil.fetchMasterAddrByClusterName(mqAdminExt, "DefaultCluster");

            // 删除每个主题
            for (String topic : topicSet) {

                System.out.println("-----------------------------------------------------------------");
                System.out.println("topic = " + topic);
                try {

                    // mqAdminExt.cleanUnusedTopic(topic);
                    mqAdminExt.deleteTopicInNameServer(brokerAddrSet, topic);

                } catch (Exception e) {

                    log.error("Failed to delete topic {} : {}", topic, e.getLocalizedMessage());

                }

            }

        } finally {

            mqAdminExt.shutdown();

        }

    }


}