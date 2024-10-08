package ptp.fltv.web.service.mq.init;

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
import org.springframework.util.StringUtils;
import ptp.fltv.web.service.mq.MqApplication;

import java.util.List;


/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/27 PM 10:17:06
 * @description 给Sentinel注册动态加载规则的数据源
 * @filename SentinelDatasourceInitFunc.java
 */

@Slf4j
public class SentinelDatasourceInitFunc implements InitFunc {


    @Override
    public void init() {

        String SELF_HOST_IP = confirmSelfHostIp();

        ReadableDataSource<String, List<FlowRule>> flowRuleDatasource = new NacosDataSource<>(SELF_HOST_IP + ":8848", "DEFAULT_GROUP", "ptp-web-service-mq-flow-rules",

                source ->
                        JSON.parseObject(source, new TypeReference<>() {
                                }

                        ));

        FlowRuleManager.register2Property(flowRuleDatasource.getProperty());

        ReadableDataSource<String, List<DegradeRule>> degradationRuleDatasource = new NacosDataSource<>(SELF_HOST_IP + ":8848", "DEFAULT_GROUP", "ptp-web-service-mq-degradation-rules",

                source ->
                        JSON.parseObject(source, new TypeReference<>() {
                                }

                        ));

        DegradeRuleManager.register2Property(degradationRuleDatasource.getProperty());

        // 2024-5-27  22:17-注册自定义的事件监听器监听熔断器状态变换事件
        EventObserverRegistry.getInstance().addStateChangeObserver("logging",
                (prevState, newState, rule, snapshotValue) ->

                        log.warn("service [{}] status : {} -> {}, snapshotValue={}", "ptp-web-service-mq", prevState.name(),
                                TimeUtil.currentTimeMillis(), snapshotValue)

        );

    }


    /**
     * @return 如果已经配置了物理IP , 则返回配置好的数据 , 否则则返回127.0.0.1
     * @author Lenovo/LiGuanda
     * @date 2024/10/9 AM 12:08:41
     * @version 1.0.0
     * @description 确定好本机的真实物理IP
     * @apiNote 该方法主要解决该类因反射实例化而导致@Value注解失效的问题
     * @filename SentinelDatasourceInitFunc.java
     */
    private String confirmSelfHostIp() {

        String configIp = MqApplication.context.getEnvironment().getProperty("ip.physical.self-host");
        return StringUtils.hasLength(configIp) ? configIp : "127.0.0.1";

    }


}
