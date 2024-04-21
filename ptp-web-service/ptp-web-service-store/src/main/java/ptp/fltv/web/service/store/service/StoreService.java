package ptp.fltv.web.service.store.service;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.io.File;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/20 PM 9:36:12
 * @description 存储服务的接口
 * @filename StoreService.java
 */

public interface StoreService {


    /**
     * @param region     区域
     * @param bucketName 存储桶名称
     * @param fileName   对象名称
     * @param storePath  本地文件存储路径
     * @param option     其他附加配置(可选)
     * @return 是否成功获取对象
     * @author Lenovo/LiGuanda
     * @date 2024/4/21 PM 9:33:40
     * @version 1.0.0
     * @description 获取指定区域的指定存储桶中的指定对象
     * @filename StoreService.java
     */
    boolean getFile(@Nullable String region, @Nonnull String bucketName, @Nonnull String fileName, @Nonnull String storePath, @Nullable Map<String, Object> option);


    /**
     * @param region     区域
     * @param bucketName 存储桶名称
     * @param storePath  云端存储路径
     * @param file       待上传的文件
     * @param option     其他配置选项(可选)
     * @return 是否成功上传对象
     * @author Lenovo/LiGuanda
     * @date 2024/4/21 PM 9:51:29
     * @version 1.0.0
     * @description 上传对象到指定区域的指定存储桶的指定位置处
     * @filename StoreService.java
     */
    boolean uploadFile(@Nullable String region, @Nonnull String bucketName, @Nonnull String storePath, @Nonnull File file, @Nullable Map<String, Object> option);


}
