package ptp.fltv.web.service;

import jakarta.annotation.Nonnull;
import org.springframework.web.multipart.MultipartFile;
import pfp.fltv.common.enums.ContentType;
import pfp.fltv.common.model.po.content.Comment;

import java.io.IOException;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/11/5 AM 12:37:23
 * @description 多媒体服务接口
 * @filename MediaService.java
 */

public interface MediaService {


    /**
     * @param contentType 多媒体数据所附属的内容实体的类型
     * @param mediaType   多媒体数据的类型
     * @param uid         多媒体数据的提交者的ID
     * @param file        多媒体数据的数据本体
     * @return 如果上传资源成功 , 则返回资源对应的云端COS资源直链 , 否则 , 上传失败则返回null
     * @throws IOException          将用户传输上来的多媒体文件保存至服务端内部指定路径时失败可能会抛出此异常
     * @throws InterruptedException 将多媒体文件上传到腾讯云对象存储COS的过程中遭到意外阻断时将会抛出此异常
     * @author Lenovo/LiGuanda
     * @date 2024/11/5 AM 1:31:27
     * @version 1.0.0
     * @description 添加单个多媒体数据
     * @filename MediaService.java
     */
    String insertSingleMedia(@Nonnull Comment.BelongType contentType, @Nonnull ContentType mediaType, @Nonnull Long uid, @Nonnull MultipartFile file) throws IOException, InterruptedException;


}