package ptp.fltv.web.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.TransferManagerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/8/11 PM 5:27:18
 * @description 腾讯云对象存储COS的相关配置
 * @filename CosConfig.java
 */

@Configuration
public class CosConfig {


    @Value("${cos.secretId}")
    public String secretId;
    @Value("${cos.secretKey}")
    public String secretKey;
    @Value("${cos.regionName}")
    public String regionName;


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/8/11 PM 5:37:45
     * @version 1.0.0
     * @description 用于操作云端资源文件的COS客户端
     * @filename CosConfig.java
     */
    @Bean
    public COSClient cosClient() {

        // 2024-8-11  17:55-TODO-目前先采用不加密的客户端进行开发 , 便于调试 , 后期需要就将其替换为KMS加密的客户端(https://cloud.tencent.com/document/product/436/53481)
        COSCredentials credentials = new BasicCOSCredentials(secretId, secretKey);
        ClientConfig clientConfig = new ClientConfig(new Region(regionName));
        clientConfig.setHttpProtocol(HttpProtocol.https);
        return new COSClient(credentials, clientConfig);

    }


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/8/11 PM 6:02:16
     * @version 1.0.0
     * @description 该实例用来后续调用高级接口去操作云端资源文件(可以实现分片上传等功能)
     * @filename CosConfig.java
     */
    @Bean
    public TransferManager transferManager() {

        COSClient cosClient = cosClient();

        // 自定义线程池大小，建议在客户端与 COS 网络充足（例如使用腾讯云的 CVM，同地域上传 COS）的情况下，设置成16或32即可，可较充分的利用网络资源
        // 对于使用公网传输且网络带宽质量不高的情况，建议减小该值，避免因网速过慢，造成请求超时。
        ExecutorService threadPool = Executors.newFixedThreadPool(32);

        // 传入一个 thread pool, 若不传入线程池，默认 TransferManager 中会生成一个单线程的线程池。
        TransferManager transferManager = new TransferManager(cosClient, threadPool);

        // 设置高级接口的配置项
        // 分块上传阈值和分块大小分别设置为 5MB 和 1MB（若不特殊设置，分块上传阈值和分块大小的默认值均为5MB）
        TransferManagerConfiguration transferManagerConfiguration = new TransferManagerConfiguration();
        transferManagerConfiguration.setMultipartUploadThreshold(5 * 1024 * 1024);
        transferManagerConfiguration.setMinimumUploadPartSize(1024 * 1024);
        transferManager.setConfiguration(transferManagerConfiguration);

        return transferManager;

    }


}