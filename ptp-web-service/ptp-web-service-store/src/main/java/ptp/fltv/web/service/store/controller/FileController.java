package ptp.fltv.web.service.store.controller;

import com.alibaba.fastjson2.JSONObject;
import io.minio.StatObjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pfp.fltv.common.enums.ContentType;
import pfp.fltv.common.response.Result;
import ptp.fltv.web.service.store.service.StoreService;
import ptp.fltv.web.service.store.utils.FileUtils;
import ptp.fltv.web.service.store.utils.ReflectUtils;

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
 * @description 媒体内容上传控制器
 * @filename FileController.java
 */

@Tag(name = "媒体OSS服务接口")
@RestController
@RequestMapping("/service/store/file")
public class FileController {


    @Autowired
    private StoreService storeService;


    @Operation(description = "下载一个文件")
    @GetMapping("/download/file")
    public void downloadFile(

            @Parameter(name = "region", description = "媒体对象所在的区域") @RequestParam(name = "region", required = false) String region,
            @Parameter(name = "bucketName", description = "媒体对象所在的存储桶", required = true) @RequestParam("bucketName") String bucketName,
            @Parameter(name = "storePath", description = "媒体对象所在桶内的相对路径", required = true) @RequestParam("storePath") String storePath,
            HttpServletResponse httpServletResponse

    ) throws IOException {

        InputStream inputStream = storeService.getFileInputStream(region, bucketName, storePath, null);

        String fileName = FileUtils.fetchFileName(storePath, true);

        httpServletResponse.setHeader("Content-Disposition", String.format("attachment;filename=%s", URLEncoder.encode(fileName, StandardCharsets.UTF_8)));
        httpServletResponse.setContentType("application/octet-stream");
        httpServletResponse.setCharacterEncoding("UTF-8");
        IOUtils.copy(inputStream, httpServletResponse.getOutputStream());

    }


    @Operation(description = "上传一个文件")
    @PostMapping("/upload/file")
    public Result<String> uploadFile(

            @Parameter(name = "file", description = "用户上传的媒体对象文件", required = true) @RequestPart("file") MultipartFile multipartFile,
            @Parameter(name = "region", description = "媒体对象所在的区域") @RequestParam(name = "region", required = false) String region,
            @Parameter(name = "bucketName", description = "媒体对象所在的存储桶", required = true) @RequestParam("bucketName") String bucketName,
            @Parameter(name = "storePath", description = "媒体对象所在桶内的相对路径", required = true) @RequestParam("storePath") String storePath

    ) throws IOException {

        Map<String, Object> options = new HashMap<>();
        options.put("size", multipartFile.getSize());

        ContentType contentType = FileUtils.fileExtension2ContentType(Objects.requireNonNull(FileUtils.fetchFileExtensionFromPath(Objects.requireNonNull(multipartFile.getOriginalFilename()), true)));

        boolean isUploadSuccessfully = storeService.uploadFile(region, bucketName, storePath, multipartFile.getInputStream(), contentType, options);

        return isUploadSuccessfully ? Result.success("File uploaded successfully!") : Result.failure("Failed to upload a file");

    }


    @Operation(description = "上传多个文件")
    @PostMapping("/upload/files")
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

            isUploadSuccessfully = storeService.uploadFile(region, bucketName, storePaths.get(i), multipartFiles.get(i).getInputStream(), contentType, options);
            if (!isUploadSuccessfully) {

                return Result.failure("An exception occurred during file uploading!");

            }

        }

        return Result.success("All files were uploaded successfully!");

    }


    @Operation(description = "删除一个文件")
    @DeleteMapping("/delete/file")
    public Result<String> deleteFile(

            @Parameter(name = "region", description = "媒体对象所在的区域") @RequestParam(name = "region", required = false) String region,
            @Parameter(name = "bucketName", description = "媒体对象所在的存储桶", required = true) @RequestParam("bucketName") String bucketName,
            @Parameter(name = "storePath", description = "媒体对象所在桶内的相对路径", required = true) @RequestParam("storePath") String storePath

    ) {

        boolean isDeleteSuccessfully = storeService.deleteFile(region, bucketName, storePath, null);

        return isDeleteSuccessfully ? Result.success("Delete file successfully!") : Result.failure("Failed to delete a file!");

    }


    @Operation(description = "删除多个文件")
    @DeleteMapping("/delete/files")
    public Result<String> deleteFiles(

            @Parameter(name = "region", description = "媒体对象所在的区域") @RequestParam(name = "region", required = false) String region,
            @Parameter(name = "bucketName", description = "媒体对象所在的存储桶", required = true) @RequestParam("bucketName") String bucketName,
            @Parameter(name = "storePaths", description = "媒体对象所在桶内的多个相对路径", required = true) @RequestParam("storePaths") List<String> storePaths

    ) {

        if (CollectionUtils.isEmpty(storePaths)) {

            return Result.failure("The storePaths parameter is empty!");

        }

        boolean isUploadSuccessfully;

        boolean isDeleteSuccessfully = storeService.deleteFiles(region, bucketName, storePaths, null);

        return isDeleteSuccessfully ? Result.success("All files were deleted successfully!") : Result.failure("Failed to delete all files!");

    }


    @Operation(description = "更新一个文件")
    @PutMapping("/update/file")
    public Result<String> updateFile(

            @Parameter(name = "file", description = "用户上传的媒体对象文件") @RequestPart("file") MultipartFile multipartFile,
            @Parameter(name = "region", description = "媒体对象所在的区域") @RequestParam(name = "region", required = false) String region,
            @Parameter(name = "bucketName", description = "媒体对象所在的存储桶", required = true) @RequestParam("bucketName") String bucketName,
            @Parameter(name = "storePath", description = "媒体对象所在桶内的相对路径", required = true) @RequestParam("storePath") String storePath

    ) throws IOException {

        ContentType contentType = FileUtils.fileExtension2ContentType(Objects.requireNonNull(FileUtils.fetchFileExtensionFromPath(Objects.requireNonNull(storePath), true)));

        boolean isUpdateSuccessfully = storeService.updateFile(region, bucketName, storePath, multipartFile.getInputStream(), contentType, null);
        return isUpdateSuccessfully ? Result.success("Update file successfully!") : Result.failure("Failed to update a file!");

    }


    @Operation(description = "获取一个文件的元信息")
    @GetMapping("/get/file/information")
    public Result<JSONObject> getFileInformation(

            @Parameter(name = "region", description = "媒体对象所在的区域") @RequestParam(name = "region", required = false) String region,
            @Parameter(name = "bucketName", description = "媒体对象所在的存储桶", required = true) @RequestParam("bucketName") String bucketName,
            @Parameter(name = "storePath", description = "媒体对象所在桶内的相对路径", required = true) @RequestParam("storePath") String storePath

    ) {

        ContentType contentType = FileUtils.fileExtension2ContentType(Objects.requireNonNull(FileUtils.fetchFileExtensionFromPath(Objects.requireNonNull(storePath), true)));

        StatObjectResponse fileInformation = storeService.getFileInformation(region, bucketName, storePath, null);

        return Result.success(ReflectUtils.toJSONObjectForcibly(fileInformation, null));

    }


    @Operation(description = "判断一个文件是否存在")
    @GetMapping("/predicate/file/exist")
    public Result<Boolean> isFileExist(

            @Parameter(name = "region", description = "媒体对象所在的区域") @RequestParam(name = "region", required = false) String region,
            @Parameter(name = "bucketName", description = "媒体对象所在的存储桶", required = true) @RequestParam("bucketName") String bucketName,
            @Parameter(name = "storePath", description = "媒体对象所在桶内的相对路径", required = true) @RequestParam("storePath") String storePath

    ) {

        ContentType contentType = FileUtils.fileExtension2ContentType(Objects.requireNonNull(FileUtils.fetchFileExtensionFromPath(Objects.requireNonNull(storePath), true)));

        boolean isFileExist = storeService.isFileExist(region, bucketName, storePath, null);

        return Result.success(isFileExist);

    }


}
