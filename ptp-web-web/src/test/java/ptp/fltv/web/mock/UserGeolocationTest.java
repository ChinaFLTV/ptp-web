package ptp.fltv.web.mock;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/22 AM 8:29:37
 * @description 与用户当前的地理位置信息操作相关的单元测试
 * @filename UserGeolocationTest.java
 */

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.StringRedisTemplate;
import pfp.fltv.common.model.po.info.AddressInfo;
import ptp.fltv.web.controller.UserController;

import java.util.Random;

@Slf4j
@SpringBootTest
public class UserGeolocationTest {


    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private UserController userController;
    private final Random random = new Random(520);
    // 2024-6-22  8:45-以中国北京的地理坐标为中心点向四周进行随机扩散
    final double CENTER_LONGITUDE = 116.41667D;
    final double CENTER_LATITUDE = 39.91667D;


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/6/22 AM 9:13:34
     * @version 1.0.0
     * @description 填充虚拟的用户当前所处的地理位置信息
     * @filename UserGeolocationTest.java
     */
    @Test
    public void fillFakeUserGeolocations() {

        log.info("Start to add user virtual geolocation...");

        final String KEY = "user:geolocation:current";
        final int COUNT = 10_000;

        // 2024-6-22  23:37-这里的AddressInfo可以抽离到for循环外面,因为每轮循环过后更新的AddressInfo都将会被更新至云端,后续再怎么改变都不会影响本轮循环产生的结果
        AddressInfo addressInfo = new AddressInfo();

        for (int i = 0; i < COUNT; i++) {

            Point point = generateFakePointRandomly();
            addressInfo.setLongitude(point.getX());
            addressInfo.setLatitude(point.getY());

            long id = random.nextLong(11);

            userController.refreshGeolocation(id, addressInfo);

        }

        log.info("Add user virtual geolocation task is complete !");

    }


    private Point generateFakePointRandomly() {

        final double minLongitude = CENTER_LONGITUDE - 30.00D;
        final double maxLongitude = CENTER_LONGITUDE + 30.00D;
        final double minLatitude = CENTER_LATITUDE - 20.00D;
        final double maxLatitude = CENTER_LATITUDE + 20.00D;

        double fakeLongitude = minLongitude + (maxLongitude - minLongitude) * random.nextDouble();
        double fakeLatitude = minLatitude + (maxLatitude - minLatitude) * random.nextDouble();

        return new Point(fakeLongitude, fakeLatitude);

    }


}