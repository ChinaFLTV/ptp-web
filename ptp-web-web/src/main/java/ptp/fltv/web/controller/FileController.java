package ptp.fltv.web.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pfp.fltv.common.annotation.LogRecord;
import pfp.fltv.common.model.po.response.Result;
import ptp.fltv.web.service.FileService;

import java.io.IOException;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/11/8 PM 3:12:09
 * @description 文件读写相关的控制器
 * @filename FileController.java
 */

@RequiredArgsConstructor
@Tag(name = "文件读写操作接口")
@RestController
@RequestMapping("/resource/file")
public class FileController {


    private final FileService fileService;


    @LogRecord(description = "预览指定相对路径上的单个文件")
    @SentinelResource("web-resource-file-controller")
    @Operation(description = "预览指定相对路径上的单个文件")
    @RequestMapping(value = "/preview/single/**", method = RequestMethod.GET)
    public void previewSingleFile(

            HttpServletRequest request,
            HttpServletResponse response
            // @Parameter(name = "relativePath", description = "待预览的文件的云端相对路径(即该文件上传时选择的相对存放路径)(路径要求见上传文件时的相对路径的要求)", in = ParameterIn.PATH, required = true) @PathVariable("relativePath") String relativePath

    ) throws IOException {

        fileService.previewSingleFile(response, request.getRequestURL().toString().split("/resource/file/preview/single/")[1]);

    }


    @LogRecord(description = "下载指定相对路径上的单个文件")
    @SentinelResource("web-resource-file-controller")
    @Operation(description = "下载指定相对路径上的单个文件")
    @RequestMapping(value = "/download/single/**", method = RequestMethod.GET)
    public void downloadSingleFile(

            HttpServletRequest request,
            HttpServletResponse response
            // @Parameter(name = "relativePath", description = "待下载的文件的云端相对路径(即该文件上传时选择的相对存放路径)(路径要求见上传文件时的相对路径的要求)", in = ParameterIn.PATH, required = true) @PathVariable("relativePath") String relativePath

    ) throws IOException {

        fileService.downloadSingleFile(response, request.getRequestURL().toString().split("/resource/file/download/single/")[1]);

    }


    @GlobalTransactional(name = "insert-single-file", rollbackFor = Exception.class)
    @LogRecord(description = "保存单个文件到指定相对路径上")
    @SentinelResource("web-resource-file-controller")
    @Operation(description = "保存单个文件到指定相对路径上")
    @PostMapping("/upload/single")
    public Result<String> uploadSingleFile(

            @Parameter(name = "relativePath", description = "用户期望上传的文件所保存的相对路径(相对于当前Web程序运行所在的路径)(路径需要包含文件完整的名称)(不要以斜杠开头)", required = true) @RequestParam("relativePath") String relativePath,
            @Parameter(name = "file", description = "用户上传到服务器端的文件", required = true) @RequestParam("file") MultipartFile file

    ) {

        String fileAccessUri = fileService.uploadSingleFile(relativePath, file);
        return fileAccessUri != null ? Result.success(fileAccessUri) : Result.failure(null);

    }


    @GlobalTransactional(name = "insert-multiple-file", rollbackFor = Exception.class)
    @LogRecord(description = "保存多个文件到指定相对路径上")
    @SentinelResource("web-resource-file-controller")
    @Operation(description = "保存多个文件到指定相对路径上")
    @PostMapping("/upload/multiple")
    public Result<List<String>> uploadMultipleFile(

            @Parameter(name = "relativePaths", description = "用户期望上传的多个文件所保存的对应的相对路径(相对于当前Web程序运行所在的路径)(路径需要包含文件完整的名称)(不要以斜杠开头)", required = true) @RequestParam("relativePaths") List<String> relativePaths,
            @Parameter(name = "file", description = "用户上传到服务器端的多个文件", required = true) @RequestParam("files") MultipartFile[] files

    ) {

        List<String> fileAccessUris = fileService.uploadMultipleFile(relativePaths, files);
        return fileAccessUris != null ? Result.success(fileAccessUris) : Result.failure(null);

    }


    @GlobalTransactional(name = "delete-single-file", rollbackFor = Exception.class)
    @LogRecord(description = "删除指定相对路径上的单个文件数据")
    @SentinelResource("web-resource-file-controller")
    @Operation(description = "删除指定相对路径上的单个文件数据")
    @RequestMapping(value = "/delete/single/**", method = RequestMethod.DELETE)
    public Result<?> deleteSingleFile(HttpServletRequest request) {

        boolean isDeleted = fileService.deleteSingleFile(request.getRequestURL().toString().split("/resource/file/delete/single/")[1]);
        return isDeleted ? Result.success(null) : Result.failure(null);

    }


}