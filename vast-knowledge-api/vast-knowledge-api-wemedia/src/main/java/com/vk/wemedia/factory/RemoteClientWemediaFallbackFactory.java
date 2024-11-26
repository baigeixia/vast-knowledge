package com.vk.wemedia.factory;


import com.vk.common.core.domain.R;
import com.vk.wemedia.domain.WmMaterialFeign;
import com.vk.wemedia.feign.RemoteClientWemediaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 用户服务降级处理
 * 
 * @author vk
 */
@Component
public class RemoteClientWemediaFallbackFactory implements FallbackFactory<RemoteClientWemediaService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteClientWemediaFallbackFactory.class);

    @Override
    public RemoteClientWemediaService create(Throwable throwable)
    {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteClientWemediaService()
        {
            @Override
            public R<Boolean> saveMaterial(WmMaterialFeign materialFeign) {
                return R.fail("保存服务失败:" + throwable.getMessage());
            }
        };
    }
}
