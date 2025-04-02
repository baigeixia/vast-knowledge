package com.vk.ai.config;


import com.volcengine.ark.runtime.service.ArkService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j(topic = "InitializeModelApi")
public class InitializeModelApi implements DisposableBean {

    @Autowired
    private ArkConfig arkConfig;

    static ConnectionPool connectionPool = new ConnectionPool(5, 1, TimeUnit.SECONDS);

    static Dispatcher dispatcher = new Dispatcher();

    private ArkService arkService;

    /**
     * 默认的   ArkService  接入
     * The interface ark service.
     * public abstract class ArkBaseService {
     *         public static final String BASE_URL = "https://ark.cn-beijing.volces.com";
     *         public static final String BASE_REGION = "cn-beijing";
     *         public static final Duration DEFAULT_TIMEOUT = Duration.ofMinutes(10);
     *         public static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofMinutes(1);
     *         public static final int DEFAULT_RETRY_TIMES = 2;
     *     }
     */
    @Bean
    public ArkService arkServiceApi() {
        String apiKey = arkConfig.getApiKey();
        String url = arkConfig.getBaseUrl();
        Integer connectTimeout = arkConfig.getConnectTimeout();
        Integer timeout = arkConfig.getTimeout();
        Integer retryTimes = arkConfig.getRetryTimes();
        // 默认补偿
        if (url.isEmpty())url="https://ark.cn-beijing.volces.com/api/v3";
        if (timeout == 0 ) connectTimeout = 10;
        if (connectTimeout == 0 ) connectTimeout = 1;
        if (retryTimes == 0 ) connectTimeout = 2;


        arkService = ArkService.builder()
                .dispatcher(dispatcher)
                .connectionPool(connectionPool)
                .baseUrl(url)
                .timeout(Duration.ofSeconds(timeout))
                .connectTimeout(Duration.ofSeconds(connectTimeout))
                .retryTimes(retryTimes)
                .apiKey(apiKey)
                .build();

        log.info("ArkService 初始化完成");

        return arkService;
    }

    @Override
    public void destroy() throws Exception {
        if (arkService != null) {
            arkService.shutdownExecutor();
            log.info("ArkService 销毁结束");
        }
    }


}
