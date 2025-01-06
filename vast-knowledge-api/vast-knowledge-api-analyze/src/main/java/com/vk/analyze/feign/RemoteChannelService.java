package com.vk.analyze.feign;

import com.vk.analyze.domain.AdChannel;
import com.vk.analyze.factory.RemoteChannelFallbackFactory;
import com.vk.common.core.constant.ServiceNameConstants;
import com.vk.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@FeignClient(contextId = "RemoteChannelService", value = ServiceNameConstants.ANALYZE_SERVICE, fallbackFactory = RemoteChannelFallbackFactory.class)
public interface RemoteChannelService {

    @RequestMapping(method = RequestMethod.GET, value = "/channel/{channelId}")
    R<AdChannel> getChannel(@PathVariable("channelId") Long channelId);

    @GetMapping("/sensitive/sensitiveShort")
    R<Map<String, AtomicInteger>> getSensitiveShort(
            @RequestParam(name = "text") String text
    );

    @GetMapping("/sensitive/sensitive")
    R<Map<String, AtomicInteger>> getSensitive(
            @RequestParam(name = "text") String text
    );
}
