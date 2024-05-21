package ptp.fltv.web;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pfp.fltv.common.model.po.finance.Commodity;
import ptp.fltv.web.mapper.CommodityMapper;

import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/21 PM 9:58:57
 * @description 多表联查SQL语句测试
 * @filename MultipleTableJoinQueryTest.java
 */

@Slf4j
@SpringBootTest
public class MultipleTableJoinQueryTest {


    @Autowired
    private CommodityMapper commodityMapper;


    @Test
    public void testCommodityMultipleTableJoinQueryCrudSql() {

        log.warn("---------------------------------testCommodityMultipleTableJoinQueryCrudSql--------------------------------");

        /*Commodity commodity = commodityMapper.getOneById(5L);
            System.out.println(commodity);*/

        /*List<Commodity> commodities = commodityMapper.getListByRange(3L, 3L, true);
        for (Commodity commodity : commodities) {

            System.out.println(commodity);

        }*/


    }


}
