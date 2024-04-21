package ptp.fltv.web.service.store.service.impl;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptp.fltv.web.service.store.service.StoreService;
import ptp.fltv.web.service.store.utils.FileUtils;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
    public boolean getFile(@Nullable String region, @Nonnull String bucketName, @Nonnull String fileName, @Nonnull String storePath, @Nullable Map<String, Object> option) {

        try (
                InputStream inputStream = minioClient.getObject(

                        GetObjectArgs.builder()
                                .region(region)
                                .bucket(bucketName)
                                .object(fileName)
                                .build()

                );
                FileOutputStream fileOutputStream = new FileOutputStream(storePath)

        ) {

            byte[] buffer = new byte[1024];
            int availableBytes;
            while ((availableBytes = inputStream.read(buffer)) != -1) {

                fileOutputStream.write(buffer, 0, availableBytes);

            }

            return true;

        } catch (RuntimeException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException |
                 ServerException | XmlParserException e) {

            log.error(e.getLocalizedMessage());
            return false;

        }

    }


    @Override
    public boolean uploadFile(@Nullable String region, @NotNull String bucketName, @NotNull String storePath, @NotNull File file, @Nullable Map<String, Object> option) {

        try (FileInputStream fileInputStream = new FileInputStream(file)) {

            String fileExtension = FileUtils.fetchFileExtensionFromPath(file.getAbsolutePath(), true);

            minioClient.putObject(

                    PutObjectArgs.builder()
                            .region(region)
                            .bucket(bucketName)
                            .object(storePath)
                            .stream(fileInputStream, file.length(), -1)
                            .contentType(FileUtils.fileExtension2ContentType(String.valueOf(fileExtension)).getDefaultContentType())
                            .build()

            );

            return true;

        } catch (RuntimeException | IOException | ServerException | InsufficientDataException | ErrorResponseException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                 InternalException e) {

            log.error(e.getLocalizedMessage());
            return false;

        }

    }

}
