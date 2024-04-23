package ptp.fltv.web.service.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pfp.fltv.common.model.dto.MediaOperationDto;
import ptp.fltv.web.service.store.service.StoreService;
import ptp.fltv.web.service.store.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/23 PM 8:43:20
 * @description 媒体内容上传控制器
 * @filename MediaController.java
 */

@Tag(name = "媒体OSS服务接口")
@RestController
@RequestMapping("/content/store")
public class MediaController {


    @Autowired
    private StoreService storeService;


    @Operation(description = "下载一个文件")
    @PostMapping("/get/file")
    public void downloadFile(@Parameter(name = "媒体对象操作数据信息") @RequestBody MediaOperationDto mediaOperationDto, HttpServletResponse httpServletResponse) throws IOException {

        InputStream inputStream = storeService.getFileInputStream(mediaOperationDto.getRegion(), mediaOperationDto.getBucketName(), mediaOperationDto.getStorePath(), null);

        String fileName = FileUtils.fetchFileName(mediaOperationDto.getStorePath(), true);

        httpServletResponse.setHeader("Content-Disposition", String.format("attachment;filename=%s", URLEncoder.encode(fileName, StandardCharsets.UTF_8)));
        httpServletResponse.setContentType("application/octet-stream");
        httpServletResponse.setCharacterEncoding("UTF-8");
        IOUtils.copy(inputStream, httpServletResponse.getOutputStream());

    }


}
