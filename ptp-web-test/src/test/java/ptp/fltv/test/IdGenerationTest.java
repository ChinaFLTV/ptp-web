package ptp.fltv.test;

import org.junit.Test;

import java.util.UUID;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/14 下午 4:27:37
 * @description 进行ID生成效率的测试
 * @filename IdGenerationTest.java
 */

public class IdGenerationTest {


    @Test
    public void testUUID() {

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 1_000_000_000; i++) {

            UUID uuid = UUID.randomUUID();

        }

        long endTime = System.currentTimeMillis();
        System.out.printf("生成十亿条UUID数据耗时：%d ms\n", endTime - startTime);// 生成十亿条UUID数据耗时：374509 ms

    }


}
