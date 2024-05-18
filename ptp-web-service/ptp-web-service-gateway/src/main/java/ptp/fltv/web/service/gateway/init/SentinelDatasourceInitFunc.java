package ptp.fltv.web.service.gateway.init;

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
 * @date 2024/5/18 PM 10:51:08
 * @description 给Sentinel注册动态加载规则的数据源
 * @filename SentinelDatasourceInitFunc.java
 */

@Slf4j
public class SentinelDatasourceInitFunc implements InitFunc {


    @Override
    public void init() {

        ReadableDataSource<String, List<FlowRule>> flowRuleDatasource = new NacosDataSource<>("127.0.0.1:8848", "DEFAULT_GROUP", "ptp-web-service-gateway-flow-rules",

                source ->
                        JSON.parseObject(source, new TypeReference<>() {
                                }

                        ));

        FlowRuleManager.register2Property(flowRuleDatasource.getProperty());

        ReadableDataSource<String, List<DegradeRule>> degradationRuleDatasource = new NacosDataSource<>("127.0.0.1:8848", "DEFAULT_GROUP", "ptp-web-service-gateway-degradation-rules",

                source ->
                        JSON.parseObject(source, new TypeReference<>() {
                                }

                        ));

        DegradeRuleManager.register2Property(degradationRuleDatasource.getProperty());

        // 2024-5-18  22:51-注册自定义的事件监听器监听熔断器状态变换事件
        EventObserverRegistry.getInstance().addStateChangeObserver("logging",
                (prevState, newState, rule, snapshotValue) ->

                        log.warn("service [{}] status : {} -> {}, snapshotValue={}", "ptp-web-service-gateway", prevState.name(),
                                TimeUtil.currentTimeMillis(), snapshotValue)

        );

    }


}
