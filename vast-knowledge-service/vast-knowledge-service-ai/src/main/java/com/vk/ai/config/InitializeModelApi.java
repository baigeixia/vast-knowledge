package com.vk.ai.config;


import com.volcengine.ark.runtime.service.ArkService;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class InitializeModelApi implements InitializingBean,DisposableBean {

    @Autowired
    private ArkConfig arkConfig;

    static String baseUrl = "https://ark.cn-beijing.volces.com/api/v3";

    static ConnectionPool connectionPool = new ConnectionPool(5, 1, TimeUnit.SECONDS);

    static Dispatcher dispatcher = new Dispatcher();

    private ArkService arkService;

    @Bean
    public ArkService arkServiceApi() {
        String apiKey = arkConfig.getApiKey();

        return ArkService.builder()
                .dispatcher(dispatcher)
                .connectionPool(connectionPool)
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .build();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        arkService = arkServiceApi();
    }

    @Override
    public void destroy() throws Exception {
        if (arkService != null) {
            arkService.shutdownExecutor();
        }
    }


}
