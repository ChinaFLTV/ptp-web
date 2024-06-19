package ptp.fltv.web.service.store.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson2.JSONObject;
import io.minio.StatObjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pfp.fltv.common.annotation.LogRecord;
import pfp.fltv.common.enums.ContentType;
import pfp.fltv.common.response.Result;
import pfp.fltv.common.utils.FileUtils;
import pfp.fltv.common.utils.ReflectUtils;
import ptp.fltv.web.service.store.service.FileService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/23 PM 8:43:20
 * @description 文件操作的控制器
 * @filename FileController.java
 */

@AllArgsConstructor
@Tag(name = "OSS文件服务接口")
@RestController
@RequestMapping("/service/store/file")
public class FileController {


    private FileService fileService;


    @LogRecord(description = "下载一个文件")
    @SentinelResource("service-store-file-controller")
    @Operation(description = "下载一个文件")
    @GetMapping("/download")
    public void downloadFile(

            @Parameter(name = "region", description = "媒体对象所在的区域") @RequestParam(name = "region", required = false) String region,
            @Parameter(name = "bucketName", description = "媒体对象所在的存储桶", required = true) @RequestParam("bucketName") String bucketName,
            @Parameter(name = "storePath", description = "媒体对象所在桶内的相对路径", required = true) @RequestParam("storePath") String storePath,
            HttpServletResponse httpServletResponse

    ) throws IOException {

        InputStream inputStream = fileService.getFileInputStream(region, bucketName, storePath, null);
        String fileName = FileUtils.fetchFileName(storePath, true);

        httpServletResponse.setHeader("Content-Disposition", String.format("attachment;filename=%s", URLEncoder.encode(fileName, StandardCharsets.UTF_8)));
        httpServletResponse.setContentType("application/octet-stream");
        httpServletResponse.setCharacterEncoding("UTF-8");
        IOUtils.copy(inputStream, httpServletResponse.getOutputStream());

    }


    @LogRecord(description = "分片下载一个文件")
    @SentinelResource("service-store-file-controller")
    @Operation(description = "分片下载一个文件")
    @GetMapping("/download/partition")
    public void downloadFilePartition(

            @Parameter(name = "region", description = "媒体对象所在的区域") @RequestParam(name = "region", required = false) String region,
            @Parameter(name = "bucketName", description = "媒体对象所在的存储桶", required = true) @RequestParam("bucketName") String bucketName,
            @Parameter(name = "storePath", description = "媒体对象所在桶内的相对路径", required = true) @RequestParam("storePath") String storePath,
            @Parameter(name = "downloadedLength", description = "先前以下载好的字节数(本次分片下载的偏移量)", required = true) @RequestParam("downloadedLength") Long downloadedLength,
            @Parameter(name = "partitionLength", description = "本次分片下载的字节数", required = true) @RequestParam("partitionLength") Long partitionLength,
            HttpServletResponse httpServletResponse

    ) throws IOException {

        byte[] bytes = fileService.downloadFilePartition(region, bucketName, storePath, downloadedLength, partitionLength, null);
        String fileName = FileUtils.fetchFileName(storePath, true);

        httpServletResponse.setHeader("Content-Disposition", String.format("attachment;filename=%s", URLEncoder.encode(fileName, StandardCharsets.UTF_8)));
        httpServletResponse.setContentType("application/octet-stream");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.getOutputStream().write(bytes);

    }


    @LogRecord(description = "上传一个文件")
    @SentinelResource("service-store-file-controller")
    @Operation(description = "上传一个文件")
    @PostMapping("/upload")
    public Result<String> uploadFile(

            @Parameter(name = "file", description = "用户上传的媒体对象文件", required = true) @RequestPart("file") MultipartFile multipartFile,
            @Parameter(name = "region", description = "媒体对象所在的区域") @RequestParam(name = "region", required = false) String region,
            @Parameter(name = "bucketName", description = "媒体对象所在的存储桶", required = true) @RequestParam("bucketName") String bucketName,
            @Parameter(name = "storePath", description = "媒体对象所在桶内的相对路径", required = true) @RequestParam("storePath") String storePath

    ) throws IOException {

        Map<String, Object> options = new HashMap<>();
        options.put("size", multipartFile.getSize());

        ContentType contentType = FileUtils.fileExtension2ContentType(Objects.requireNonNull(FileUtils.fetchFileExtensionFromPath(Objects.requireNonNull(multipartFile.getOriginalFilename()), true)));
        boolean isUploadSuccessfully = fileService.uploadFile(region, bucketName, storePath, multipartFile.getInputStream(), contentType, options);
        return isUploadSuccessfully ? Result.success("File uploaded successfully!") : Result.failure("Failed to upload a file");

    }


    @LogRecord(description = "上传多个文件")
    @SentinelResource("service-store-file-controller")
    @Operation(description = "上传多个文件")
    @PostMapping("/upload/batch")
    public Result<String> uploadFiles(

            @Parameter(name = "files", description = "用户上传的多个媒体对象文件", required = true) @RequestPart("files") List<MultipartFile> multipartFiles,
            @Parameter(name = "region", description = "媒体对象所在的区域") @RequestParam(name = "region", required = false) String region,
            @Parameter(name = "bucketName", description = "媒体对象所在的存储桶", required = true) @RequestParam("bucketName") String bucketName,
            @Parameter(name = "storePaths", description = "媒体对象所在桶内的多个相对路径", required = true) @RequestParam("storePaths") List<String> storePaths

    ) throws IOException {


        // 2024-4-24  19:40-这里直接调用minio的上传文件的API，一方面是因为如果再去单独封装一个上传多个文件的服务API，使用起来很不舒服，因为控制器方法这边接收的参数还得需要进行多个预处理
        // 于是乎，如果将这些步骤并入到服务API中，反而会降低代码的可复用性，如果不封装，直接在控制器这边处理好像又不是那么的优雅。
        // 最后决定选择后者
        if (multipartFiles == null || storePaths == null || multipartFiles.size() != storePaths.size()) {

            return Result.failure("Some parameters are invalid!");

        }

        Map<String, Object> options = new HashMap<>();
        boolean isUploadSuccessfully;

        for (int i = 0; i < multipartFiles.size(); i++) {

            options.put("size", multipartFiles.get(i).getSize());
            ContentType contentType = FileUtils.fileExtension2ContentType(Objects.requireNonNull(FileUtils.fetchFileExtensionFromPath(Objects.requireNonNull(storePaths.get(i)), true)));
            isUploadSuccessfully = fileService.uploadFile(region, bucketName, storePaths.get(i), multipartFiles.get(i).getInputStream(), contentType, options);
            if (!isUploadSuccessfully) {

                return Result.failure("An exception occurred during file uploading!");

            }

        }

        return Result.success("All files were uploaded successfully!");

    }


    @LogRecord(description = "删除一个文件")
    @SentinelResource("service-store-file-controller")
    @Operation(description = "删除一个文件")
    @DeleteMapping("/delete")
    public Result<String> deleteFile(

            @Parameter(name = "region", description = "媒体对象所在的区域") @RequestParam(name = "region", required = false) String region,
            @Parameter(name = "bucketName", description = "媒体对象所在的存储桶", required = true) @RequestParam("bucketName") String bucketName,
            @Parameter(name = "storePath", description = "媒体对象所在桶内的相对路径", required = true) @RequestParam("storePath") String storePath

    ) {

        boolean isDeleteSuccessfully = fileService.deleteFile(region, bucketName, storePath, null);
        return isDeleteSuccessfully ? Result.success("Delete file successfully!") : Result.failure("Failed to delete a file!");

    }


    @LogRecord(description = "删除多个文件")
    @SentinelResource("service-store-file-controller")
    @Operation(description = "删除多个文件")
    @DeleteMapping("/delete/batch")
    public Result<String> deleteFiles(

            @Parameter(name = "region", description = "媒体对象所在的区域") @RequestParam(name = "region", required = false) String region,
            @Parameter(name = "bucketName", description = "媒体对象所在的存储桶", required = true) @RequestParam("bucketName") String bucketName,
            @Parameter(name = "storePaths", description = "媒体对象所在桶内的多个相对路径", required = true) @RequestParam("storePaths") List<String> storePaths

    ) {

        if (CollectionUtils.isEmpty(storePaths)) {

            return Result.failure("The storePaths parameter is empty!");

        }

        boolean isDeleteSuccessfully = fileService.deleteFiles(region, bucketName, storePaths, null);
        return isDeleteSuccessfully ? Result.success("All files were deleted successfully!") : Result.failure("Failed to delete all files!");

    }


    @LogRecord(description = "更新一个文件")
    @SentinelResource("service-store-file-controller")
    @Operation(description = "更新一个文件")
    @PutMapping("/update")
    public Result<String> updateFile(

            @Parameter(name = "file", description = "用户上传的媒体对象文件") @RequestPart("file") MultipartFile multipartFile,
            @Parameter(name = "region", description = "媒体对象所在的区域") @RequestParam(name = "region", required = false) String region,
            @Parameter(name = "bucketName", description = "媒体对象所在的存储桶", required = true) @RequestParam("bucketName") String bucketName,
            @Parameter(name = "storePath", description = "媒体对象所在桶内的相对路径", required = true) @RequestParam("storePath") String storePath

    ) throws IOException {

        ContentType contentType = FileUtils.fileExtension2ContentType(Objects.requireNonNull(FileUtils.fetchFileExtensionFromPath(Objects.requireNonNull(storePath), true)));
        boolean isUpdateSuccessfully = fileService.updateFile(region, bucketName, storePath, multipartFile.getInputStream(), contentType, null);
        return isUpdateSuccessfully ? Result.success("Update file successfully!") : Result.failure("Failed to update a file!");

    }


    @LogRecord(description = "获取一个文件的元信息")
    @SentinelResource("service-store-file-controller")
    @Operation(description = "获取一个文件的元信息")
    @GetMapping("/get/information")
    public Result<JSONObject> getFileInformation(

            @Parameter(name = "region", description = "媒体对象所在的区域") @RequestParam(name = "region", required = false) String region,
            @Parameter(name = "bucketName", description = "媒体对象所在的存储桶", required = true) @RequestParam("bucketName") String bucketName,
            @Parameter(name = "storePath", description = "媒体对象所在桶内的相对路径", required = true) @RequestParam("storePath") String storePath

    ) {

        ContentType contentType = FileUtils.fileExtension2ContentType(Objects.requireNonNull(FileUtils.fetchFileExtensionFromPath(Objects.requireNonNull(storePath), true)));
        StatObjectResponse fileInformation = fileService.getFileInformation(region, bucketName, storePath, null);
        return Result.success(ReflectUtils.toJSONObjectForcibly(fileInformation, null));

    }


    @LogRecord(description = "判断一个文件是否存在")
    @SentinelResource("service-store-file-controller")
    @Operation(description = "判断一个文件是否存在")
    @GetMapping("/exist")
    public Result<Boolean> isFileExist(

            @Parameter(name = "region", description = "媒体对象所在的区域") @RequestParam(name = "region", required = false) String region,
            @Parameter(name = "bucketName", description = "媒体对象所在的存储桶", required = true) @RequestParam("bucketName") String bucketName,
            @Parameter(name = "storePath", description = "媒体对象所在桶内的相对路径", required = true) @RequestParam("storePath") String storePath

    ) {

        ContentType contentType = FileUtils.fileExtension2ContentType(Objects.requireNonNull(FileUtils.fetchFileExtensionFromPath(Objects.requireNonNull(storePath), true)));
        boolean isFileExist = fileService.isFileExist(region, bucketName, storePath, null);
        return Result.success(isFileExist);

    }


}
