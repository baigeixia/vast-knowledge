package com.vk.analyze.feign;

import com.vk.analyze.domain.AdChannel;
import com.vk.analyze.factory.RemoteChannelFallbackFactory;
import com.vk.common.core.constant.ServiceNameConstants;
import com.vk.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(contextId = "RemoteChannelService", value = ServiceNameConstants.ANALYZE_SERVICE, fallbackFactory = RemoteChannelFallbackFactory.class)
public interface RemoteChannelService {

    @RequestMapping(method = RequestMethod.GET, value = "/channel/{channelId}")
    R<AdChannel> getChannel(@PathVariable("channelId") Long channelId);

}
