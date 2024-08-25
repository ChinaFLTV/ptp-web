package ptp.fltv.web;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pfp.fltv.common.utils.NetworkUtils;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/8/24 PM 5:51:20
 * @description 该测试主要与网络相关
 * @filename NetworkTest.java
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NetworkTest {


    @Test
    public void testGetIp() {

        String ip = NetworkUtils.getRealIP();
        System.out.println("ip = " + ip);

    }


}