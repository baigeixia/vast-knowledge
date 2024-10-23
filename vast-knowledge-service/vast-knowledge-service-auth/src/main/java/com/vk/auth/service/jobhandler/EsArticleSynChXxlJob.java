package com.vk.auth.service.jobhandler;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class EsArticleSynChXxlJob {

    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("EsArticleSynCh")
    public void demoJobHandler() {

    }
}
