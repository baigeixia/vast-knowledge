package com.vk.wemedia.feign;

import com.vk.common.core.constant.SecurityConstants;
import com.vk.common.core.constant.ServiceNameConstants;
import com.vk.common.core.domain.R;
import com.vk.wemedia.domain.WmMaterialFeign;
import com.vk.wemedia.factory.RemoteClientWemediaFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(contextId = "remoteClientWemediaService", value = ServiceNameConstants.ARTICLE_WEMEDIA, fallbackFactory = RemoteClientWemediaFallbackFactory.class)
public interface RemoteClientWemediaService {

    @PostMapping("/material/save")
    R<Boolean> saveMaterial(@RequestBody WmMaterialFeign materialFeign);

}
