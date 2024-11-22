package com.vk.config;

import com.tencent.cloud.CosStsClient;
import com.tencent.cloud.Credentials;
import com.tencent.cloud.Response;
import com.tencent.cloud.Scope;
import com.tencent.cloud.cos.util.Jackson;
import com.vk.common.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j(topic = "TxCOSClient")
public class TxCOSClient {
    @Autowired
    private TxDfsConfig txDfsConfig;

    @Autowired
    private RedisService redisService;

    @Bean
    public Credentials getCredential () {
        String c_KEY = "TX_Credentials";
        Credentials credentials = redisService.getCacheObject(c_KEY);
        if (!ObjectUtils.isEmpty(credentials)){
            return credentials;
        }
        TreeMap<String, Object> config = new TreeMap<String, Object>();

        try {
            Properties properties = new Properties();
            File configFile = new File("local.properties");
            properties.load(new FileInputStream(configFile));

            // 固定密钥 SecretId
            config.put("secretId", properties.getProperty("SecretId"));
            // 固定密钥 SecretKey
            config.put("secretKey", properties.getProperty("SecretKey"));

            if (properties.containsKey("https.proxyHost")) {
                System.setProperty("https.proxyHost", properties.getProperty("https.proxyHost"));
                System.setProperty("https.proxyPort", properties.getProperty("https.proxyPort"));
            }

            // 临时密钥有效时长，单位是秒
            config.put("durationSeconds", properties.getProperty("durationSeconds"));

            //设置 policy
            List<Scope> scopes = new ArrayList<Scope>();
            scopes.add(new Scope("*", properties.getProperty("bucket"), properties.getProperty("region"), "*"));
            config.put("policy", CosStsClient.getPolicy(scopes));

            Response credential = CosStsClient.getCredential(config);
            Credentials upCredentials = credential.credentials;
            redisService.setCacheObject(c_KEY,upCredentials,credential.expiredTime, TimeUnit.SECONDS);
            return upCredentials;
        } catch (Exception e) {
            log.error("no valid secret ! :{}",e.getMessage());
        }
    }



}
