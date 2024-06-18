package ptp.fltv.web;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pfp.fltv.common.enums.LoginClientType;
import pfp.fltv.common.utils.JwtUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/18 PM 7:28:13
 * @description 有目的的生成开发所需要的签名过后的数据字符串
 * @filename JwtTest.java
 */

@SpringBootTest
public class JwtTest {


    @Test
    public void encodeLoginEnvData() {

        Map<String, Object> nativeEnvMap1 = new HashMap<>();
        nativeEnvMap1.put("client-type", LoginClientType.WEB);
        nativeEnvMap1.put("device-id", "7758521");
        nativeEnvMap1.put("login-datetime", LocalDateTime.now());

        String encodedNativeEnvMap1 = JwtUtils.encode(nativeEnvMap1);

        Map<String, Object> nativeEnvMap2 = new HashMap<>();
        nativeEnvMap2.put("client-type", LoginClientType.WEB);
        nativeEnvMap2.put("device-id", "5201314");
        nativeEnvMap2.put("login-datetime", LocalDateTime.now());

        String encodedNativeEnvMap2 = JwtUtils.encode(nativeEnvMap2);


        System.out.println("--------------------------------encodeLoginEnvData---------------------------------");
        System.out.println("encodedNativeEnvMap1 : ");
        System.out.println(encodedNativeEnvMap1);
        System.out.println("encodedNativeEnvMap2 : ");
        System.out.println(encodedNativeEnvMap2);

    }


}