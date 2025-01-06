package com.vk.analyze.factory;


import com.vk.analyze.domain.AdChannel;
import com.vk.analyze.feign.RemoteChannelService;
import com.vk.common.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用户服务降级处理
 * 
 * @author vk
 */
@Component
public class RemoteChannelFallbackFactory implements FallbackFactory<RemoteChannelService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteChannelFallbackFactory.class);

    @Override
    public RemoteChannelService create(Throwable throwable)
    {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteChannelService()
        {
            @Override
            public R<AdChannel> getChannel(Long channelId) {
                return R.fail("获取频道信息失败"+throwable.getMessage());
            }

            @Override
            public R<Map<String, AtomicInteger>> getSensitiveShort(String text) {
                return R.fail("敏感词检测错误"+throwable.getMessage());
            }

            @Override
            public R<Map<String, AtomicInteger>> getSensitive(String text) {
                return R.fail("敏感词检测错误"+throwable.getMessage());
            }
        };
    }
}
