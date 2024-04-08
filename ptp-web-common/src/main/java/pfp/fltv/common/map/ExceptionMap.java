package pfp.fltv.common.map;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/8 上午 10:23:33
 * @description 存储错误码和错误消息的对组
 * @filename ExceptionMap.java
 */

@Slf4j
@Component
public class ExceptionMap {


    public static final Map<Integer, String> MAP = new HashMap<>();


    static {

        log.info("---->\t\t\t开始加载异常映射文件到缓存中");
        InputStream inputStream = ExceptionMap.class.getClassLoader().getResourceAsStream("config/ptp_exception_reference.json");
        JSONArray jsonArray = JSON.parseArray(inputStream);
        for (int i = 0; i < jsonArray.size(); i++) {

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Integer code = jsonObject.getInteger("code");
            String message = jsonObject.getString("message");

            MAP.put(code, message);

        }
        log.info("<----\t\t\t加载异常映射文件到缓存中完成");

    }


}
