package ptp.fltv.web.service.store.service.impl;

import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.RemoveBucketArgs;
import io.minio.errors.*;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptp.fltv.web.service.store.service.StoreService;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/20 PM 9:36:45
 * @description 存储服务实现
 * @filename StoreServiceImpl.java
 */

@Slf4j
@Service
public class StoreServiceImpl implements StoreService {


    @Autowired
    private MinioClient minioClient;


    @Override
    public boolean createBucket(@NotNull String bucketName, @Nullable String region) {

        try {

            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(bucketName)
                            .region(region)
                            .build()
            );

            return true;

        } catch (ErrorResponseException | InsufficientDataException | InternalException | IOException |
                 NoSuchAlgorithmException | ServerException | XmlParserException | InvalidResponseException |
                 InvalidKeyException e) {

            log.error(e.getLocalizedMessage());
            return false;

        }

    }


    @Override
    public boolean removeBucket(@NotNull String bucketName, @Nullable String region) {

        try {

            minioClient.removeBucket(
                    RemoveBucketArgs.builder()
                            .bucket(bucketName)
                            .region(region)
                            .build()
            );

            return true;

        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | NoSuchAlgorithmException | IOException | ServerException |
                 XmlParserException e) {

            log.error(e.getLocalizedMessage());
            return false;

        }

    }


}
