package ptp.fltv.web;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.fastjson2.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/18 PM 8:42:05
 * @description 生成Sentinel的限流、降级和熔断规则的JSON字符串的测试
 * @filename GenerateSentinelRuleTest.java
 */

@SpringBootTest
public class GenerateSentinelRuleTest {


    private final String[] resources = new String[]{

            "web-content-test",
            "web-content-announcement-controller",
            "web-content-asset-controller",
            "web-content-dialogue-controller",
            "web-gate-controller",
            "web-content-passage-comment-controller",
            "web-content-passage-controller",
            "web-content-user-role-controller",
            "web-content-user-controller"

    };


    @Test
    public void testGenerateSentinelFlowRuleTestJSONString() throws IOException {

        final List<FlowRule> flowRules = new ArrayList<>();

        for (String resource : resources) {

            FlowRule flowRule = new FlowRule();
            flowRule.setResource(resource);
            flowRule.setCount(10);
            flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
            flowRule.setStrategy(RuleConstant.STRATEGY_DIRECT);

            flowRules.add(flowRule);

        }

        // 2024-5-18  21:26-这里为了方便，直接写资源文件的绝对路径以直接将输出的JSON写到编译前项目的Resource目录中去而不是编译后的Resource目录里
        // ClassPathResource flowRulesJsonResource = new ClassPathResource("ptp-web-web-flow-rules.json");
        // JSON.writeTo(new FileOutputStream(flowRulesJsonResource.getFile()), JSON.toJSONString(flowRuleList));
        JSON.writeTo(new FileOutputStream("D:/JavaProjects/ptp-web-backend/ptp-web-web/src/test/resources/ptp-web-web-flow-rules.json", false), flowRules);


    }


    @Test
    public void testGenerateSentinelDegradationRuleTestJSONString() throws IOException {

        final List<DegradeRule> degradeRules = new ArrayList<>();

        for (String resource : resources) {

            DegradeRule degradeRule = new DegradeRule();
            degradeRule.setResource(resource);
            degradeRule.setTimeWindow(10);
            degradeRule.setMinRequestAmount(5);
            degradeRule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO);
            degradeRule.setCount(0.5);
            degradeRule.setStatIntervalMs(60_000);

            degradeRules.add(degradeRule);

        }

        // ClassPathResource degradationRulesJsonResource = new ClassPathResource("ptp-web-web-degradation-rules.json");
        // JSON.writeTo(new FileOutputStream(degradationRulesJsonResource.getFile()), degradeRules);
        JSON.writeTo(new FileOutputStream("D:/JavaProjects/ptp-web-backend/ptp-web-web/src/test/resources/ptp-web-web-degradation-rules.json", false), degradeRules);

    }


}
