package ptp.fltv.web.service.store.service;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/20 PM 9:36:12
 * @description 存储服务的接口
 * @filename StoreService.java
 */

public interface StoreService {


    /**
     * @param bucketName 存储桶名称
     * @param region     存储桶所在区域
     * @return 是否创建成功
     * @author Lenovo/LiGuanda
     * @date 2024/4/20 PM 10:09:56
     * @version 1.0.0
     * @description 创建存储桶
     * @filename StoreService.java
     */
    boolean createBucket(@Nonnull String bucketName, @Nullable String region);


    /**
     * @param bucketName 存储桶名称
     * @param region     存储桶所在区域
     * @return 是否删除成功
     * @author Lenovo/LiGuanda
     * @date 2024/4/20 PM 10:26:40
     * @version 1.0.0
     * @description 删除存储桶
     * @filename StoreService.java
     */
    boolean removeBucket(@Nonnull String bucketName, @Nullable String region);


}
