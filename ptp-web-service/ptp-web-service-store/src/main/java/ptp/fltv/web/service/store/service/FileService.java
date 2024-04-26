package ptp.fltv.web.service.store.service;

import io.minio.StatObjectResponse;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.jetbrains.annotations.NotNull;
import pfp.fltv.common.enums.ContentType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/20 PM 9:36:12
 * @description 存储服务(文件)的接口
 * @filename StoreService.java
 */

public interface FileService {


    /**
     * @param region     区域
     * @param bucketName 存储桶名称
     * @param storePath  云端文件存储路径
     * @param option     其他附加配置(可选)
     * @return 是否成功获取对象的输入流
     * @author Lenovo/LiGuanda
     * @date 2024/4/21 PM 9:33:40
     * @version 1.0.0
     * @description 获取指定区域的指定存储桶中的指定对象的数据输入流(内部使用的网络资源的数据流)
     * @filename StoreService.java
     */
    InputStream getFileInputStream(@Nullable String region, @Nonnull String bucketName, @Nonnull String storePath, @Nullable Map<String, Object> option);


    /**
     * @param region     区域
     * @param bucketName 存储桶名称
     * @param fileName   对象名称
     * @param storePath  本地文件存储路径
     * @param option     其他附加配置(可选)
     * @return 是否成功下载对象
     * @author Lenovo/LiGuanda
     * @date 2024/4/22 PM 10:16:26
     * @version 1.0.0
     * @description 下载指定区域的指定存储桶中的指定对象(内部直接调用minio的下载对象的API)
     * @filename StoreService.java
     */
    boolean downloadFile(@Nullable String region, @Nonnull String bucketName, @Nonnull String fileName, @Nonnull String storePath, @Nullable Map<String, Object> option);


    /**
     * @param region           区域
     * @param bucketName       存储桶名称
     * @param storePath        本地文件存储路径
     * @param downloadedLength 先前以下载好的字节数(本次分片下载的偏移量)
     * @param partitionLength  本次分片下载的字节数
     * @param option           其他附加配置(可选)
     * @return 成功下载对象的一部分
     * @implNote 前端需要多次调用该方法以完成断点下载，每次下载都必须记录起始点和下载长度，并确保所有分片都存储在一个地方(建立放在单个目录中，目录以etag命名)，方便后续的汇总
     * @author Lenovo/LiGuanda
     * @date 2024/4/26 PM 8:05:27
     * @version 1.0.0
     * @description 分片下载指定区域的指定存储桶中的指定对象(适合分片下载 ， 节约流量带宽)
     * @filename FileService.java
     */
    byte[] downloadFilePartition(@Nullable String region, @Nonnull String bucketName, @Nonnull String storePath, @Nonnull Long downloadedLength, @Nonnull Long partitionLength, @Nullable Map<String, Object> option);


    /**
     * @param region      区域
     * @param bucketName  存储桶名称
     * @param storePath   云端存储路径(相对于存储桶)
     * @param inputStream 待上传的文件的输入流
     * @param contentType 文件所属的媒体类型
     * @param option      其他配置选项(可选)
     * @return 是否成功上传对象
     * @author Lenovo/LiGuanda
     * @date 2024/4/21 PM 9:51:29
     * @version 1.0.0
     * @description 上传对象到指定区域的指定存储桶的指定位置处(桶内目录不存在将会自动创建)
     * @filename StoreService.java
     */
    boolean uploadFile(@Nullable String region, @Nonnull String bucketName, @Nonnull String storePath, @NotNull InputStream inputStream, ContentType contentType, @Nullable Map<String, Object> option);


    /**
     * @param region          区域
     * @param bucketName      存储桶名称
     * @param storePath       云端存储路径(相对于存储桶)
     * @param inputStream     待上传的文件的输入流
     * @param contentType     文件所属的媒体类型
     * @param uploadedLength  先前以上传好的字节数(本次分片上传的偏移量)
     * @param partitionLength 本次分片上传的字节数
     * @param option          其他配置选项(可选)
     * @return 是否成功上传本片对象数据
     * @author Lenovo/LiGuanda
     * @implNote 前端那边必须要求送过来的文件流是已经裁切好的，不能是文件的全部，这样做跟一次性上传有什么区别。同时，前端那边还要注意上传路径命名的规范性，严格遵循：{文件的Hash}-{序号} 的格式
     * @date 2024/4/26 PM 10:02:46
     * @version 1.0.0
     * @description 分片上传对象到指定区域的指定存储桶的指定位置处(桶内目录不存在将会自动创建)
     * @filename FileService.java
     */
    boolean uploadFilePartition(@Nullable String region, @Nonnull String bucketName, @Nonnull String storePath, @NotNull InputStream inputStream, @Nonnull Long uploadedLength, @Nonnull Long partitionLength, ContentType contentType, @Nullable Map<String, Object> option);


    /**
     * @param region      区域
     * @param bucketName  存储桶名称
     * @param storePaths  多个云端存储路径(相对于存储桶)
     * @param files       待上传的多个文件
     * @param contentType 文件所属的媒体类型
     * @param option      其他配置选项(可选)
     * @return 是否成功上传全部的指定对象
     * @throws FileNotFoundException 输入流为空时将会抛出该异常
     * @author Lenovo/LiGuanda
     * @date 2024/4/22 PM 10:04:31
     * @version 1.0.0
     * @description 批量上传对象到指定区域的指定存储桶的指定位置处(目前只支持批量上传同类型的文件)
     * @filename StoreService.java
     * @deprecated 鸡肋的服务方法，不要也罢
     */
    @Deprecated
    boolean uploadFiles(@Nullable String region, @Nonnull String bucketName, @Nonnull List<String> storePaths, @Nonnull List<File> files, ContentType contentType, @Nullable Map<String, Object> option) throws FileNotFoundException;


    /**
     * @param region     区域
     * @param bucketName 存储桶名称
     * @param storePath  云端存储路径(相对于存储桶，必须唯一同一存储桶下)
     * @param option     其他配置选项(可选)
     * @return 是否成功删除对象
     * @author Lenovo/LiGuanda
     * @date 2024/4/22 PM 10:01:07
     * @version 1.0.0
     * @description 删除指定预取的指定存储桶的指定对象
     * @filename StoreService.java
     */
    boolean deleteFile(@Nullable String region, @Nonnull String bucketName, @Nonnull String storePath, @Nullable Map<String, Object> option);


    /**
     * @param region     区域
     * @param bucketName 存储桶名称
     * @param storePaths 多个云端存储路径(相对于存储桶，必须唯一同一存储桶下)
     * @param option     其他配置选项(可选)
     * @return 是否成功删除全部指定的对象
     * @author Lenovo/LiGuanda
     * @date 2024/4/22 PM 9:11:00
     * @version 1.0.0
     * @description 批量删除指定预取的指定存储桶的指定对象
     * @filename StoreService.java
     */
    boolean deleteFiles(@Nullable String region, @Nonnull String bucketName, @Nonnull List<String> storePaths, @Nullable Map<String, Object> option);


    /**
     * @param region      区域
     * @param bucketName  存储桶名称
     * @param storePath   云端存储路径(相对于存储桶)
     * @param contentType 文件所属的媒体类型
     * @param option      其他配置选项(可选)
     * @return 是否成功更新对象
     * @throws FileNotFoundException 输入流为空时将会抛出该异常
     * @author Lenovo/LiGuanda
     * @date 2024/4/22 PM 9:55:45
     * @version 1.0.0
     * @description 更新指定区域的指定存储桶中的指定对象(不存在则创建该对象)
     * @filename StoreService.java
     */
    boolean updateFile(@Nullable String region, @Nonnull String bucketName, @Nonnull String storePath, @Nonnull InputStream inputStream, ContentType contentType, @Nullable Map<String, Object> option) throws FileNotFoundException;


    /**
     * @param region     区域
     * @param bucketName 存储桶名称
     * @param storePath  云端存储路径(相对于存储桶)
     * @param option     其他配置选项(可选)
     * @return 云端媒体对象的详细信息
     * @author Lenovo/LiGuanda
     * @date 2024/4/24 PM 9:13:34
     * @version 1.0.0
     * @description 获取指定区域的指定存储桶的指定对象的元信息
     * @filename StoreService.java
     */
    StatObjectResponse getFileInformation(@Nullable String region, @Nonnull String bucketName, @Nonnull String storePath, @Nullable Map<String, Object> option);


    /**
     * @param region     区域
     * @param bucketName 存储桶名称
     * @param storePath  云端存储路径(相对于存储桶)
     * @param option     其他配置选项(可选)
     * @return 云端媒体对象是否存在
     * @author Lenovo/LiGuanda
     * @date 2024/4/24 PM 10:21:08
     * @version 1.0.0
     * @description 判断指定区域的指定存储桶的指定对象是否存在
     * @filename StoreService.java
     */
    boolean isFileExist(@Nullable String region, @Nonnull String bucketName, @Nonnull String storePath, @Nullable Map<String, Object> option);


}
