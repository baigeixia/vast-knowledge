package com.vk.ai.config;


import com.volcengine.ark.runtime.service.ArkService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j(topic = "InitializeFZModelApi")
public class InitializeFZModelApi implements DisposableBean {

    @Autowired
    private ArkConfig arkConfig;

    static ConnectionPool connectionPool = new ConnectionPool(5, 1, TimeUnit.SECONDS);

    static Dispatcher dispatcher = new Dispatcher();

    private ArkService arkService;

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
