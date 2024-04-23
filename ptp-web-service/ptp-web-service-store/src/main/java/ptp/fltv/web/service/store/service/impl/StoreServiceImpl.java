package ptp.fltv.web.service.store.service.impl;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import pfp.fltv.common.enums.ContentType;
import ptp.fltv.web.service.store.service.StoreService;
import ptp.fltv.web.service.store.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    public InputStream getFileInputStream(@Nullable String region, @Nonnull String bucketName, @Nonnull String storePath, @Nullable Map<String, Object> option) {

        try {

            GetObjectArgs.Builder builder = GetObjectArgs.builder();

            if (StringUtils.hasLength(region)) {

                builder.region(region);

            }

            builder.bucket(bucketName)
                    .object(storePath);

            return minioClient.getObject(builder.build());

        } catch (RuntimeException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException |
                 ServerException | XmlParserException e) {

            log.error(e.getLocalizedMessage());
            return null;

        }

    }


    @Override
    public boolean downloadFile(@Nullable String region, @Nonnull String bucketName, @Nonnull String
            fileName, @Nonnull String storePath, @Nullable Map<String, Object> option) {

        try {

            minioClient.downloadObject(

                    DownloadObjectArgs.builder()
                            .region(region)
                            .bucket(bucketName)
                            .object(fileName)
                            .filename(storePath)
                            .build()

            );

            return true;

        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                 InternalException e) {

            log.error(e.getLocalizedMessage());
            return false;

        }

    }


    @Override
    public boolean uploadFile(@Nullable String region, @NotNull String bucketName, @NotNull String
            storePath, @NotNull File file, ContentType contentType, @Nullable Map<String, Object> option) {

        try (FileInputStream fileInputStream = new FileInputStream(file)) {

            String fileExtension = FileUtils.fetchFileExtensionFromPath(file.getAbsolutePath(), true);

            minioClient.putObject(

                    PutObjectArgs.builder()
                            .region(region)
                            .bucket(bucketName)
                            .object(storePath)
                            .stream(fileInputStream, file.length(), -1)
                            .contentType(contentType.getDefaultContentType())
                            .build()

            );

            return true;

        } catch (RuntimeException | IOException | ServerException | InsufficientDataException |
                 ErrorResponseException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                 InternalException e) {

            log.error(e.getLocalizedMessage());
            return false;

        }

    }


    @Override
    public boolean uploadFiles(@Nullable String region, @Nonnull String
            bucketName, @Nonnull List<String> storePaths, @Nonnull List<File> files, ContentType
                                       contentType, @Nullable Map<String, Object> option) {

        if (!CollectionUtils.isEmpty(storePaths) && !CollectionUtils.isEmpty(files) && storePaths.size() == files.size()) {

            for (int i = 0; i < storePaths.size(); i++) {

                boolean isUploadSuccessfully = uploadFile(region, bucketName, storePaths.get(i), files.get(i), contentType, option);
                if (!isUploadSuccessfully) {

                    return false;

                }

            }

        }

        return true;

    }


    @Override
    public boolean deleteFile(@Nullable String region, @Nonnull String bucketName, @Nonnull String
            storePath, @Nullable Map<String, Object> option) {

        return deleteFiles(region, bucketName, List.of(storePath), option);

    }


    @Override
    public boolean deleteFiles(@Nullable String region, @NotNull String
            bucketName, @NotNull List<String> storePaths, @Nullable Map<String, Object> option) {

        if (!CollectionUtils.isEmpty(storePaths)) {

            try {

                List<DeleteObject> deleteObjects = new ArrayList<>();
                for (String storePath : storePaths) {

                    deleteObjects.add(new DeleteObject(storePath));

                }

                Iterable<Result<DeleteError>> results = minioClient.removeObjects(

                        RemoveObjectsArgs.builder()
                                .region(region)
                                .bucket(bucketName)
                                .objects(deleteObjects)
                                .build()

                );

                if (results.iterator().hasNext()) {

                    Iterator<Result<DeleteError>> iterator = results.iterator();
                    do {

                        Result<DeleteError> errorResult = iterator.next();
                        log.error(errorResult.get().message());

                    } while (iterator.hasNext());
                    return false;

                }

                return true;

            } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                     NoSuchAlgorithmException | InvalidKeyException | XmlParserException |
                     InvalidResponseException |
                     InternalException e) {

                log.error(e.getLocalizedMessage());
                return false;

            }

        }

        return true;

    }


    @Override
    public boolean updateFile(@Nullable String region, @Nonnull String bucketName, @Nonnull String
            storePath, @Nonnull File file, ContentType contentType, @Nullable Map<String, Object> option) {

        boolean isDeleteSuccessfully = deleteFiles(region, bucketName, List.of(storePath), option);
        if (isDeleteSuccessfully) {

            return uploadFile(region, bucketName, storePath, file, contentType, option);

        }

        return false;

    }


}
