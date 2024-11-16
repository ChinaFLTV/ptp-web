package ptp.fltv.web.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pfp.fltv.common.annotation.LogRecord;
import pfp.fltv.common.enums.ContentType;
import pfp.fltv.common.model.po.content.Comment;
import pfp.fltv.common.model.po.response.Result;
import ptp.fltv.web.service.MediaService;

import java.io.IOException;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/11/5 AM 12:33:07
 * @description 多媒体控制器
 * @filename MediaController.java
 */

@RequiredArgsConstructor
@Tag(name = "多媒体操作接口")
@RestController
@RequestMapping("/resource/media")
public class MediaController {


    private final MediaService mediaService;


    @GlobalTransactional(name = "insert-single-media", rollbackFor = Exception.class)
    @LogRecord(description = "添加单个多媒体数据")
    @SentinelResource("web-resource-media-controller")
    @Operation(description = "添加单个多媒体数据")
    @PostMapping("/insert/single")
    public Result<String> insertSingleMedia(

            @Parameter(name = "contentType", description = "多媒体数据所附属的内容实体的类型", required = true) @RequestParam("contentType") Comment.BelongType contentType,
            @Parameter(name = "mediaType", description = "多媒体数据的类型", required = true) @RequestParam("mediaType") ContentType mediaType,
            @Parameter(name = "uid", description = "多媒体数据的提交者的ID", required = true) @RequestParam("uid") Long uid,
            @Parameter(name = "file", description = "多媒体数据的数据本体", required = true) @RequestParam("file") MultipartFile file

    ) throws IOException, InterruptedException {

        String mediaCosUrl = mediaService.insertSingleMedia(contentType, mediaType, uid, file);
        return StringUtils.hasLength(mediaCosUrl) ? Result.success(mediaCosUrl) : Result.failure(null);

    }


    @GlobalTransactional(name = "insert-multiple-media", rollbackFor = Exception.class)
    @LogRecord(description = "添加多个多媒体数据")
    @SentinelResource("web-resource-media-controller")
    @Operation(description = "添加多个多媒体数据")
    @PostMapping("/insert/multiple")
    public Result<List<String>> insertMultipleMedia(

            @Parameter(name = "contentType", description = "多媒体数据所附属的内容实体的类型", required = true) @RequestParam("contentType") Comment.BelongType contentType,
            @Parameter(name = "mediaType", description = "多媒体数据的类型", required = true) @RequestParam("mediaType") ContentType mediaType,
            @Parameter(name = "uid", description = "多媒体数据的提交者的ID", required = true) @RequestParam("uid") Long uid,
            @Parameter(name = "files", description = "多个多媒体数据的数据本体", required = true) @RequestParam("files") MultipartFile[] files

    ) throws IOException, InterruptedException {

        List<String> mediaCosUrls = mediaService.insertMultipleMedia(contentType, mediaType, uid, files);
        return mediaCosUrls != null ? Result.success(mediaCosUrls) : Result.failure(null);

    }


}
