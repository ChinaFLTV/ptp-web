package ptp.fltv.web;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ptp.fltv.web.service.CommodityService;

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
    private CommodityService commodityService;


    @Test
    public void testCommodityMultipleTableJoinQueryCrudSql() {

        log.warn("---------------------------------testCommodityMultipleTableJoinQueryCrudSql--------------------------------");

        /*Commodity commodity = commodityMapper.getOneById(5L);
            System.out.println(commodity);*/

        /*List<Commodity> commodities = commodityMapper.getListByRange(3L, 3L, true);
        for (Commodity commodity : commodities) {

            System.out.println(commodity);

        }*/

        /*Commodity commodity = new Commodity().setName("The Greatest Work")
                .setBrand("Jay")
                .setUserId(6L)
                .setColor("golden")
                .setMaterial("plastic")
                .setBarcode("489262519")
                .setCategory(List.of("music", "album"))
                .setDescription("Jay Chou's latest masterpiece")
                .setPrice(99.99);
        int insertedOne = commodityService.insertOne(commodity);
        System.out.println("insertedOne = " + insertedOne);*/

        /*int deleteOne = commodityService.deleteOne(1793635090897141762L);
        System.out.println("deleteOne = " + deleteOne);*/

        /*Commodity commodity = commodityService.getOneById(1793635090897141762L);
        System.out.println("commodity = " + commodity);

        commodity.setName(commodity.getName() + " [pro]");
        int updateOne = commodityService.updateOne(commodity);
        System.out.println("updateOne = " + updateOne);*/

        /*List<Commodity> commodities = commodityService.list();
        for (Commodity commodity : commodities) {

            System.out.println(commodity);

        }*/


    }


}
