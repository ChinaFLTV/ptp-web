package ptp.fltv.web;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/8 上午 8:59:55
 * @description 测试读取各种类型的文件
 * @filename ReadFileTest.java
 */

@SpringBootTest
public class ReadFileTest {


    @Test
    public void testReadJsonFile() {

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("ptp_exception_reference.json");
        JSONArray jsonArray = JSON.parseArray(inputStream);

        System.out.println("inputStream = " + inputStream);
        System.out.println("jsonArray = " + jsonArray);

    }


}
