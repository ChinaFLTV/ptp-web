package ptp.fltv.web.service.store.service;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/25 PM 8:02:05
 * @description 存储服务(存储桶)的接口
 * @filename BucketService.java
 */

public interface BucketService {


    /**
     * @param region     区域
     * @param bucketName 存储桶名称
     * @param options    其他附加配置(可选)
     * @return 是否成功创建存储桶
     * @author Lenovo/LiGuanda
     * @date 2024/4/25 PM 8:05:10
     * @version 1.0.0
     * @description 在指定区域中创建一个存储桶
     * @filename BucketService.java
     */
    boolean createBucket(@Nullable String region, @Nonnull String bucketName, @Nullable Map<String, Object> options);


    /**
     * @param region     区域
     * @param bucketName 存储桶名称
     * @param options    其他附加配置(可选)
     * @return 存储桶是否存在
     * @implNote options中的可添加参数：objectLock(是否启用对象锁)
     * @author Lenovo/LiGuanda
     * @date 2024/4/25 PM 8:12:18
     * @version 1.0.0
     * @description 判断指定区域中的存储桶是否存在
     * @filename BucketService.java
     */
    boolean isBucketExist(@Nullable String region, @Nonnull String bucketName, @Nullable Map<String, Object> options);


    /**
     * @param region     区域
     * @param bucketName 存储桶名称
     * @param options    其他附加配置(可选)
     * @return 是否成功删除存储桶
     * @author Lenovo/LiGuanda
     * @date 2024/4/25 PM 8:46:58
     * @version 1.0.0
     * @description 删除指定区域中的一个存储桶
     * @filename BucketService.java
     */
    boolean removeBucket(@Nullable String region, @Nonnull String bucketName, @Nullable Map<String, Object> options);


    /**
     * @param region     区域
     * @param bucketName 存储桶名称
     * @param options    其他附加配置(可选)
     * @return 获取到的存储桶的信息
     * @author Lenovo/LiGuanda
     * @date 2024/4/25 PM 8:53:27
     * @version 1.0.0
     * @description 获取指定区域中的一个存储桶的信息
     * @filename BucketService.java
     */
    JSONObject getBucketInformation(@Nullable String region, @Nonnull String bucketName, @Nullable Map<String, Object> options);


}
