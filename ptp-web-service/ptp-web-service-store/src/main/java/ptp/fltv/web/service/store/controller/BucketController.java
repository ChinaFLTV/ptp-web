package ptp.fltv.web.service.store.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pfp.fltv.common.response.Result;
import ptp.fltv.web.service.store.service.BucketService;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/25 PM 7:53:37
 * @description 桶操作的控制器
 * @filename BucketController.java
 */

@Tag(name = "OSS存储桶服务接口")
@RestController
@RequestMapping("/service/store/bucket")
public class BucketController {


    @Autowired
    private BucketService bucketService;


    @Operation(description = "创建一个存储桶")
    @PostMapping("/create")
    public Result<Boolean> createBucket(

            @Parameter(name = "region") @RequestParam(name = "region", required = false) String region,
            @Parameter(name = "bucketName", required = true) @RequestParam(name = "bucketName") String bucketName

    ) {

        boolean isCreateSuccessfully = bucketService.createBucket(region, bucketName, null);
        return Result.success(isCreateSuccessfully);

    }


    @Operation(description = "判断存储桶是否存在")
    @PostMapping("/exist")
    public Result<Boolean> isBucketExist(

            @Parameter(name = "region") @RequestParam(name = "region", required = false) String region,
            @Parameter(name = "bucketName", required = true) @RequestParam(name = "bucketName") String bucketName

    ) {

        boolean isExist = bucketService.isBucketExist(region, bucketName, null);
        return Result.success(isExist);

    }


    @Operation(description = "删除一个存储桶")
    @PostMapping("/remove")
    public Result<Boolean> removeBucket(

            @Parameter(name = "region") @RequestParam(name = "region", required = false) String region,
            @Parameter(name = "bucketName", required = true) @RequestParam(name = "bucketName") String bucketName

    ) {

        boolean isRemoveSuccessfully = bucketService.removeBucket(region, bucketName, null);
        return Result.success(isRemoveSuccessfully);

    }


    @Operation(description = "查看一个存储桶的信息")
    @PostMapping("/get/information")
    public Result<JSONObject> getBucketInformation(

            @Parameter(name = "region") @RequestParam(name = "region", required = false) String region,
            @Parameter(name = "bucketName", required = true) @RequestParam(name = "bucketName") String bucketName

    ) {

        return Result.success(bucketService.getBucketInformation(region, bucketName, null));

    }


    @Operation(description = "查看全部区域的全部存储桶的信息")
    @PostMapping("/get/information/all")
    public Result<JSONArray> getAllBucketInformation() {

        return Result.success(bucketService.getAllBucketsInformation(null));

    }


}
