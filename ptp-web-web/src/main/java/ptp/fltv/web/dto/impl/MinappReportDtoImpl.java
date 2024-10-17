package ptp.fltv.web.dto.impl;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import pfp.fltv.common.model.po.manage.Report;
import ptp.fltv.web.dto.ReportDto;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/10/17 PM 6:01:25
 * @description 举报DTO接口实现(知晓云)
 * @filename MinappReportDtoImpl.java
 * @deprecated 由于知晓云的WEB API需要使用域名 , 因此这里转而采用内建的数据库进行存储 , 暂时不再考虑数据上云
 */

@RequiredArgsConstructor
@Repository
public class MinappReportDtoImpl implements ReportDto {


    @Value("${minapp.client-id}")
    private String clientId;
    @Value("${minapp.client-secret}")
    private String clientSecret;
    private final RestTemplate restTemplate; // 2024-10-17  19:47-RestTemplate使用参考 : https://www.cnblogs.com/dw3306/p/16362908.html
    private final String INSERT_SINGLE_REPORT_URL = "/hserve/v2.2/table/report/record/";


    @Override
    public boolean save(@Nonnull Report report) {

        RequestEntity<Object> requestEntity = RequestEntity.post(INSERT_SINGLE_REPORT_URL)
                .header("X-Hydrogen-Client-ID", clientId)
                .header("Authorization", String.format("Hydrogen-r1 %s", clientSecret))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(report);

        ResponseEntity<JSONObject> response = restTemplate.exchange(requestEntity, JSONObject.class);
        return false;

    }


}