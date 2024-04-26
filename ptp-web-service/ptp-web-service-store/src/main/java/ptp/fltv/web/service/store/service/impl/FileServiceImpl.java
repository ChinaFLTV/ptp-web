package ptp.fltv.web.service.store.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import ptp.fltv.web.service.store.service.FileService;
import ptp.fltv.web.service.store.utils.FileUtils;

import java.io.*;
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
 * @description 存储服务(文件)的接口实现
 * @filename StoreServiceImpl.java
 */

@Slf4j
@Service
public class FileServiceImpl implements FileService {


    @Autowired
    private MinioClient minioClient;
    private static ServerSideEncryptionKms SSE;

    static {

        try {

            SSE = new ServerSideEncryptionKms("ptp-backend-minio-sse-key", null);

        } catch (JsonProcessingException e) {

            log.error(e.getCause() == null ? e.getLocalizedMessage() : e.getCause().getMessage());
            SSE = null;

        }

    }


    @Override
    public InputStream getFileInputStream(@Nullable String region, @Nonnull String bucketName, @Nonnull String storePath, @Nullable Map<String, Object> option) {

        try {

            GetObjectArgs.Builder builder = GetObjectArgs.builder()
                    .region(StringUtils.hasLength(region) ? region : null)
                    .bucket(bucketName)
                    .object(storePath);

            return minioClient.getObject(builder.build());

        } catch (RuntimeException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException |
                 ServerException | XmlParserException e) {

            log.error(e.getCause() == null ? e.getLocalizedMessage() : e.getCause().getMessage());
            return null;

        }

    }


    @Override
    public boolean downloadFile(@Nullable String region, @NotNull String bucketName, @Nonnull String fileName, @Nonnull String storePath, @Nullable Map<String, Object> option) {

        try {

            DownloadObjectArgs.Builder builder = DownloadObjectArgs.builder()
                    .region(StringUtils.hasLength(region) ? region : null)
                    .bucket(bucketName)
                    .object(storePath)
                    .filename(fileName);

            minioClient.downloadObject(builder.build());

            return true;

        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                 InternalException e) {

            log.error(e.getCause() == null ? e.getLocalizedMessage() : e.getCause().getMessage());
            return false;

        }

    }


    @Override
    public byte[] downloadFilePartition(@Nullable String region, @Nonnull String bucketName, @Nonnull String storePath, @Nonnull Long downloadedLength, @Nonnull Long partitionLength, @Nullable Map<String, Object> option) {

        try {

            GetObjectArgs.Builder builder = GetObjectArgs.builder()
                    .region(StringUtils.hasLength(region) ? region : null)
                    .bucket(bucketName)
                    .object(storePath)
                    .offset(downloadedLength)
                    .length(partitionLength);

            GetObjectResponse objectResponse = minioClient.getObject(builder.build());

            byte[] buffer = new byte[1024];
            int readByteSize = -1;
            return objectResponse.readAllBytes();

        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                 InternalException e) {

            log.error(e.getCause() == null ? e.getLocalizedMessage() : e.getCause().getMessage());
            return new byte[0];

        }

    }


    @Override
    public boolean uploadFile(@Nullable String region, @NotNull String bucketName, @NotNull String
            storePath, @NotNull InputStream inputStream, ContentType contentType, @Nullable Map<String, Object> option) {

        try (inputStream) {

            String fileExtension = FileUtils.fetchFileExtensionFromPath(storePath, true);

            PutObjectArgs.Builder builder = PutObjectArgs.builder()
                    .region(StringUtils.hasLength(region) ? region : null)
                    .bucket(bucketName)
                    // .sse(SSE)
                    .object(storePath)
                    .stream(inputStream, option == null ? inputStream.available() : (Long) option.getOrDefault("size", inputStream.available()), -1)
                    .contentType(contentType.getDefaultContentType());

            minioClient.putObject(builder.build());

            return true;

        } catch (RuntimeException | IOException | ServerException | InsufficientDataException |
                 ErrorResponseException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                 InternalException e) {

            log.error(e.getCause() == null ? e.getLocalizedMessage() : e.getCause().getMessage());
            return false;

        }

    }

    @Override
    public boolean uploadFilePartition(@Nullable String region, @NotNull String bucketName, @NotNull String storePath, @NotNull InputStream inputStream, @NotNull Long uploadedLength, @NotNull Long partitionLength, ContentType contentType, @Nullable Map<String, Object> option) {

        try (inputStream) {

            String fileExtension = FileUtils.fetchFileExtensionFromPath(storePath, true);

            PutObjectArgs.Builder builder = PutObjectArgs.builder()
                    .region(StringUtils.hasLength(region) ? region : null)
                    .bucket(bucketName)
                    // .sse(SSE)
                    .object(storePath)
                    .stream(inputStream, option == null ? inputStream.available() : (Long) option.getOrDefault("size", inputStream.available()), -1)
                    .contentType(contentType.getDefaultContentType());

            minioClient.putObject(builder.build());

            return true;

        } catch (RuntimeException | IOException | ServerException | InsufficientDataException |
                 ErrorResponseException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                 InternalException e) {

            log.error(e.getCause() == null ? e.getLocalizedMessage() : e.getCause().getMessage());
            return false;

        }

    }


    @Override
    public boolean uploadFiles(@Nullable String region, @Nonnull String
            bucketName, @Nonnull List<String> storePaths, @Nonnull List<File> files, ContentType
                                       contentType, @Nullable Map<String, Object> option) throws FileNotFoundException {

        if (!CollectionUtils.isEmpty(storePaths) && !CollectionUtils.isEmpty(files) && storePaths.size() == files.size()) {

            for (int i = 0; i < storePaths.size(); i++) {

                boolean isUploadSuccessfully = uploadFile(region, bucketName, storePaths.get(i), new FileInputStream(files.get(i)), contentType, option);
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

                RemoveObjectsArgs.Builder builder = RemoveObjectsArgs.builder()
                        .region(StringUtils.hasLength(region) ? region : null)
                        .bucket(bucketName)
                        .objects(deleteObjects);

                Iterable<Result<DeleteError>> results = minioClient.removeObjects(builder.build());

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

                log.error(e.getCause() == null ? e.getLocalizedMessage() : e.getCause().getMessage());
                return false;

            }

        }

        return true;

    }


    @Override
    public boolean updateFile(@Nullable String region, @Nonnull String bucketName, @Nonnull String
            storePath, @Nonnull InputStream inputStream, ContentType contentType, @Nullable Map<String, Object> option) {

        boolean isDeleteSuccessfully = deleteFiles(region, bucketName, List.of(storePath), option);
        if (isDeleteSuccessfully) {

            return uploadFile(region, bucketName, storePath, inputStream, contentType, option);

        }

        return false;

    }


    @Override
    public StatObjectResponse getFileInformation(@Nullable String region, @NotNull String bucketName, @NotNull String storePath, @Nullable Map<String, Object> option) {

        try {

            StatObjectArgs.Builder builder = StatObjectArgs.builder()
                    .region(StringUtils.hasLength(region) ? region : null)
                    .bucket(bucketName)
                    .object(storePath);

            return minioClient.statObject(builder.build());

        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {

            log.error(e.getCause() == null ? e.getLocalizedMessage() : e.getCause().getMessage());
            return null;

        }

    }


    @Override
    public boolean isFileExist(@Nullable String region, @NotNull String bucketName, @NotNull String storePath, @Nullable Map<String, Object> option) {

        return getFileInformation(region, bucketName, storePath, option) != null;

    }


}
