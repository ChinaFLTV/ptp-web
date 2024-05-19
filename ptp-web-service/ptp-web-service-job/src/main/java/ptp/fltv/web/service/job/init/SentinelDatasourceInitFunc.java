package ptp.fltv.web.service.job.init;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.EventObserverRegistry;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.util.TimeUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/19 PM 9:22:27
 * @description 给Sentinel注册动态加载规则的数据源
 * @filename SentinelDatasourceInitFunc.java
 */

@Slf4j
public class SentinelDatasourceInitFunc implements InitFunc {


    @Override
    public void init() {

        ReadableDataSource<String, List<FlowRule>> flowRuleDatasource = new NacosDataSource<>("127.0.0.1:8848", "DEFAULT_GROUP", "ptp-web-service-job-flow-rules",

                source ->
                        JSON.parseObject(source, new TypeReference<>() {
                                }

                        ));

        FlowRuleManager.register2Property(flowRuleDatasource.getProperty());

        ReadableDataSource<String, List<DegradeRule>> degradationRuleDatasource = new NacosDataSource<>("127.0.0.1:8848", "DEFAULT_GROUP", "ptp-web-service-job-degradation-rules",

                source ->
                        JSON.parseObject(source, new TypeReference<>() {
                                }

                        ));

        DegradeRuleManager.register2Property(degradationRuleDatasource.getProperty());

        // 2024-5-19  21:23-注册自定义的事件监听器监听熔断器状态变换事件
        EventObserverRegistry.getInstance().addStateChangeObserver("logging",
                (prevState, newState, rule, snapshotValue) ->

                        log.warn("service [{}] status : {} -> {}, snapshotValue={}", "ptp-web-service-job", prevState.name(),
                                TimeUtil.currentTimeMillis(), snapshotValue)

        );

    }


}
