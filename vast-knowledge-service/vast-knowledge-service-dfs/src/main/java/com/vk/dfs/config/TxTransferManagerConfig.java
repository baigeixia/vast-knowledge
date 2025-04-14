package com.vk.dfs.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.TransferManagerConfiguration;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@Slf4j
public class TxTransferManagerConfig {

    @Autowired
    private  COSClient cosClient;
    private TransferManager transferManager;

    @Bean
    public TransferManager createTransferManager() {
        // 自定义线程池大小，建议在客户端与 COS 网络充足（例如使用腾讯云的 CVM，同地域上传 COS）的情况下，设置成16或32即可，可较充分的利用网络资源
        // 对于使用公网传输且网络带宽质量不高的情况，建议减小该值，避免因网速过慢，造成请求超时。
        ExecutorService threadPool = Executors.newFixedThreadPool(32);
        // 传入一个 threadpool, 若不传入线程池，默认 TransferManager 中会生成一个单线程的线程池。
         transferManager = new TransferManager(cosClient, threadPool);
        // 设置高级接口的配置项
        // 分块上传阈值和分块大小分别设置为 5MB 和 1MB（若不特殊设置，分块上传阈值和分块大小的默认值均为5MB）
        TransferManagerConfiguration transferManagerConfiguration = new TransferManagerConfiguration();
        transferManagerConfiguration.setMultipartUploadThreshold(5 * 1024 * 1024);
        transferManagerConfiguration.setMinimumUploadPartSize(1024 * 1024);
        transferManager.setConfiguration(transferManagerConfiguration);
        log.info("TransferManager initialized.");
        return transferManager;
    }

    @PreDestroy
    public void shutdownTransferManager() {
        // 指定参数为 true, 则同时会关闭 transferManager 内部的 COSClient 实例。
        // 指定参数为 false, 则不会关闭 transferManager 内部的 COSClient 实例。
        if (transferManager != null) {
            transferManager.shutdownNow(true);
            log.info("TransferManager shutdown.");
        }
    }

}
