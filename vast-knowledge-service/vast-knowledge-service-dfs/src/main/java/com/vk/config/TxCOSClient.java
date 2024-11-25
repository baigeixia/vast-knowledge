package com.vk.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicSessionCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.region.Region;
import com.tencent.cloud.CosStsClient;
import com.tencent.cloud.Credentials;
import com.tencent.cloud.Response;
import com.tencent.cloud.Scope;
import com.vk.common.redis.service.RedisService;
import com.vk.domain.TxCredentials;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j(topic = "TxCOSClient")
public class TxCOSClient {
    @Autowired
    private TxDfsConfig txDfsConfig;
    @Autowired
    private RedisService redisService;

    private COSClient cosClient;

    @Bean
    public COSClient createCos() {
        if (ObjectUtils.isEmpty(cosClient)){
            Credentials credentials = getCredential().getCredentials();
            try {
                COSCredentials cred = new BasicSessionCredentials(credentials.tmpSecretId, credentials.tmpSecretKey, credentials.sessionToken);
                ClientConfig clientConfig = new ClientConfig(new Region("ap-guangzhou"));
                // 以下的设置，是可选的：
                // 设置 socket 读取超时，默认 30s
                // clientConfig.setSocketTimeout(30*1000);
                // 设置建立连接超时，默认 30s
                // clientConfig.setConnectionTimeout(30*1000);
                // 如果需要的话，设置 http 代理，ip 以及 port
                // clientConfig.setHttpProxyIp("httpProxyIp");
                // clientConfig.setHttpProxyPort(80);
                cosClient = new COSClient(cred, clientConfig);
                return cosClient;
            } catch (CosClientException e) {
                log.error("初始化 COSClient 失败 ：{}", e.getMessage());
                throw new IllegalStateException("Unable to initialize COSClient", e);
            }
        }

        return cosClient;
    }

    @Scheduled(fixedDelay = 29 * 60 * 1000)  // 每 29 分钟刷新一次（即 1740 秒）
    public void refreshTemporaryCredentials() {
        if (!ObjectUtils.isEmpty(cosClient)){
            try {
                String cacheKey = "TX_Cos_Response";
                TxCredentials response = redisService.getCacheObject(cacheKey);
                Credentials credential;
                if (ObjectUtils.isEmpty(response) || isCredentialsExpiringSoon(response) ) {
                    // 如果缓存中没有密钥或密钥即将过期，生成新的密钥
                     credential = getCredential().credentials;
                }else {
                    credential=response.getCredentials();
                }

                cosClient.setCOSCredentials(new BasicSessionCredentials(
                        credential.tmpSecretId,
                        credential.tmpSecretKey,
                        credential.sessionToken
                ));
                // 更新 COSClient 的临时密钥
                log.info("COSClient credentials refreshed successfully.");
            } catch (Exception e) {
                log.error("Failed to refresh COSClient credentials: {}", e.getMessage(), e);
            }
        }
    }

    private boolean isCredentialsExpiringSoon(TxCredentials credentials) {
        long currentTime = System.currentTimeMillis() / 1000;
        return (credentials.getExpiredTime() - currentTime) < 300; // 如果剩余有效期少于 5 分钟，认为即将过期
    }



    public TxCredentials getCredential() {
        String cacheKey = "TX_Cos_Response";

        TreeMap<String, Object> config = new TreeMap<>();
        try {
            // 固定密钥 SecretId
            config.put("secretId", txDfsConfig.getSecretId());
            // 固定密钥 SecretKey
            config.put("secretKey", txDfsConfig.getSecretKey());

            // 临时密钥有效时长，单位是秒
            config.put("durationSeconds", txDfsConfig.getDurationSeconds());

            // 设置 policy
            List<Scope> scopes = new ArrayList<>();
            scopes.add(new Scope("*", txDfsConfig.getBucket(), txDfsConfig.getRegion(), "*"));
            config.put("policy", CosStsClient.getPolicy(scopes));

            Response upResponse = CosStsClient.getCredential(config);
            Credentials reCred = upResponse.credentials;

            TxCredentials credentials = new TxCredentials();
            credentials.setCredentials(reCred);
            credentials.setExpiration(upResponse.expiration);
            credentials.setExpiredTime(upResponse.expiredTime);
            credentials.setStartTime(upResponse.startTime);

            redisService.setCacheObject(cacheKey, credentials, upResponse.expiredTime - System.currentTimeMillis() / 1000, TimeUnit.SECONDS);
            return credentials;

        } catch (Exception e) {
            log.error("no valid secret ! :{}", e.getMessage());
            throw new IllegalStateException("Unable to initialize Credentials", e);
        }
    }



    @PreDestroy
    public void closeCosClient() {
        if (cosClient != null) {
            cosClient.shutdown();
        }
    }

}
