package ptp.fltv.web.service.impl;

import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.transfer.TransferManager;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pfp.fltv.common.enums.ContentType;
import pfp.fltv.common.model.po.content.Comment;
import ptp.fltv.web.constants.CosConstants;
import ptp.fltv.web.service.MediaService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/11/5 AM 12:42:03
 * @description 多媒体服务接口的实现类
 * @filename MediaServiceImpl.java
 */

@RequiredArgsConstructor
@Service
public class MediaServiceImpl implements MediaService {


    private final TransferManager transferManager;


    @Override
    public String insertSingleMedia(@Nonnull Comment.BelongType contentType, @Nonnull ContentType mediaType, @Nonnull Long uid, @Nonnull MultipartFile file) throws IOException, InterruptedException {

        String fileName = Objects.requireNonNull(file.getOriginalFilename());
        // 2024-11-5  17:18-获取到文件的拓展名 , 并将其作为云端资源的拓展名 , 注意 , 这里前端方面必须确保用户提交的资源的实际类型与所提交到的分类必须一致 , 后端不会对文件拓展名进行合法性校验
        String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        StringBuilder key = new StringBuilder()
                .append(contentType.name().toLowerCase())
                .append("/")
                .append(mediaType.name().toLowerCase().replace("_", "-"))
                .append("/")
                .append(uid).append("-").append(System.currentTimeMillis()).append(".").append(extensionName);

        PutObjectRequest request = new PutObjectRequest(CosConstants.BUCKET_CODAILY, key.toString(), file.getInputStream(), null);
        UploadResult uploadResult = transferManager.upload(request).waitForUploadResult();

        return String.format(CosConstants.PUBLIC_REQUEST_URL_PREFIX, CosConstants.BUCKET_CODAILY, "/" + key);

    }


    @Override
    public List<String> insertMultipleMedia(@Nonnull Comment.BelongType contentType, @Nonnull ContentType mediaType, @Nonnull Long uid, @Nonnull MultipartFile[] files) throws IOException, InterruptedException {

        List<String> urls = new ArrayList<>();

        for (MultipartFile file : files) {

            urls.add(insertSingleMedia(contentType, mediaType, uid, file)); // 2024-11-16  20:14-这里为了简化业务逻辑 , 便直接采用现成的上传多媒体文件的API接口

        }

        return urls;

    }


}