package ptp.fltv.web.service.store.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pfp.fltv.common.utils.ReflectUtils;
import ptp.fltv.web.service.store.service.BucketService;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/25 PM 8:02:43
 * @description 存储服务(存储桶)的接口实现
 * @filename BucketServiceImpl.java
 */

@AllArgsConstructor
@Slf4j
@Service
public class BucketServiceImpl implements BucketService {


    private MinioClient minioClient;


    @Override
    public boolean createBucket(@Nullable String region, @NotNull String bucketName, @Nullable Map<String, Object> options) {

        try {

            // 2024-4-25  20:18-重复创建同一个存储桶直接返回false
            if (isBucketExist(region, bucketName, options)) {

                return false;

            }

            MakeBucketArgs.Builder builder = MakeBucketArgs.builder()
                    .region(StringUtils.hasLength(region) ? region : null)
                    .bucket(bucketName);

            if (options != null) {

                boolean objectLock = (Boolean) options.getOrDefault("objectLock", false);
                builder.objectLock(objectLock);

            }

            minioClient.makeBucket(builder.build());
            return true;

        } catch (ErrorResponseException | InsufficientDataException | InvalidKeyException | InternalException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {

            log.error(e.getCause() == null ? e.getLocalizedMessage() : e.getCause().getMessage());
            return false;

        }

    }


    @Override
    public boolean isBucketExist(@Nullable String region, @NotNull String bucketName, @Nullable Map<String, Object> options) {

        try {

            BucketExistsArgs.Builder builder = BucketExistsArgs.builder()
                    .region(StringUtils.hasLength(region) ? region : null)
                    .bucket(bucketName);

            return minioClient.bucketExists(builder.build());

        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {

            log.error(e.getCause() == null ? e.getLocalizedMessage() : e.getCause().getMessage());
            // 2024-4-25  20:14-注意！到此步骤说明用户查询过程出现异常，为了避免后续可能出现的意外操作，这里返回true以告知用户打消添加的念头
            // 不返回false是因为用户删除一个不存在的存储桶的影响大于添加一个存储桶的影响(当然删除一个不存在的桶和添加一个已存在的桶的影响是同等的)。
            return true;

        }

    }


    @Override
    public boolean removeBucket(@Nullable String region, @NotNull String bucketName, @Nullable Map<String, Object> options) {

        try {

            RemoveBucketArgs.Builder builder = RemoveBucketArgs.builder()
                    .region(StringUtils.hasLength(region) ? region : null)
                    .bucket(bucketName);

            minioClient.removeBucket(builder.build());
            return true;

        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {

            log.error(e.getCause() == null ? e.getLocalizedMessage() : e.getCause().getMessage());
            return false;

        }

    }


    @Override
    public JSONObject getBucketInformation(@Nullable String region, @NotNull String bucketName, @Nullable Map<String, Object> options) {

        try {

            GetBucketEncryptionArgs.Builder builder1 = GetBucketEncryptionArgs.builder()
                    .region(StringUtils.hasLength(region) ? region : null)
                    .bucket(bucketName);
            GetBucketLifecycleArgs.Builder builder2 = GetBucketLifecycleArgs.builder()
                    .region(StringUtils.hasLength(region) ? region : null)
                    .bucket(bucketName);
            GetBucketNotificationArgs.Builder builder3 = GetBucketNotificationArgs.builder()
                    .region(StringUtils.hasLength(region) ? region : null)
                    .bucket(bucketName);
            GetBucketPolicyArgs.Builder builder4 = GetBucketPolicyArgs.builder()
                    .region(StringUtils.hasLength(region) ? region : null)
                    .bucket(bucketName);
            GetBucketReplicationArgs.Builder builder5 = GetBucketReplicationArgs.builder()
                    .region(StringUtils.hasLength(region) ? region : null)
                    .bucket(bucketName);
            GetBucketTagsArgs.Builder builder6 = GetBucketTagsArgs.builder()
                    .region(StringUtils.hasLength(region) ? region : null)
                    .bucket(bucketName);
            GetBucketVersioningArgs.Builder builder7 = GetBucketVersioningArgs.builder()
                    .region(StringUtils.hasLength(region) ? region : null)
                    .bucket(bucketName);
            GetObjectLockConfigurationArgs.Builder builder8 = GetObjectLockConfigurationArgs.builder()
                    .region(StringUtils.hasLength(region) ? region : null)
                    .bucket(bucketName);

            SseConfiguration config1;
            try {

                config1 = minioClient.getBucketEncryption(builder1.build());

            } catch (Exception e) {

                config1 = null;

            }
            LifecycleConfiguration config2;
            try {

                config2 = minioClient.getBucketLifecycle(builder2.build());

            } catch (Exception e) {

                config2 = null;

            }

            NotificationConfiguration config3;
            try {

                config3 = minioClient.getBucketNotification(builder3.build());

            } catch (Exception e) {

                config3 = null;

            }

            String config4;
            try {

                config4 = minioClient.getBucketPolicy(builder4.build());

            } catch (Exception e) {

                config4 = null;

            }

            ReplicationConfiguration config5;
            try {

                config5 = minioClient.getBucketReplication(builder5.build());

            } catch (Exception e) {

                config5 = null;

            }

            Tags config6;
            try {

                config6 = minioClient.getBucketTags(builder6.build());

            } catch (Exception e) {

                config6 = null;

            }

            VersioningConfiguration config7;
            try {

                config7 = minioClient.getBucketVersioning(builder7.build());

            } catch (Exception e) {

                config7 = null;

            }

            ObjectLockConfiguration config8;
            try {

                config8 = minioClient.getObjectLockConfiguration(builder8.build());

            } catch (Exception e) {

                config8 = null;

            }

            if (options == null) {

                options = new HashMap<>();

            } else {

                // 2024-4-28  17:48-清除用户传入的已使用完毕的冗余参数，以免产生副作用
                options.clear();

            }
            String[] defaultFieldNames = {"SseConfiguration", "LifecycleConfiguration", "NotificationConfiguration", "BucketPolicy",
                    "ReplicationConfiguration", "Tags", "VersioningConfiguration", "ObjectLockConfiguration"};

            return ReflectUtils.toJSONObjectForciblyInBulk(options, false, config1, config2, config3, config4, config5, config6, config7, config8);

        } catch (Exception e) {

            log.error(e.getCause() == null ? e.getLocalizedMessage() : e.getCause().getMessage());
            return null;

        }

    }


    @Override
    public JSONArray getAllBucketsInformation(@Nullable Map<String, Object> options) {

        try {

            ListBucketsArgs.Builder builder = ListBucketsArgs.builder();
            JSONArray jsonArray = new JSONArray();

            List<Bucket> bucketList = minioClient.listBuckets(builder.build());
            for (Bucket bucket : bucketList) {

                jsonArray.add(ReflectUtils.toJSONObjectForcibly(bucket, null));

            }

            return jsonArray;

        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {

            log.error(e.getCause() == null ? e.getLocalizedMessage() : e.getCause().getMessage());
            return null;

        }

    }


}
