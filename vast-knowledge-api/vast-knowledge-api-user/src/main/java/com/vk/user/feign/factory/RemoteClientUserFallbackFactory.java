package com.vk.user.feign.factory;


import com.vk.common.core.domain.R;
import com.vk.user.feign.RemoteClientUserService;
import com.vk.user.feign.domain.ClientApUser;
import com.vk.user.feign.model.LoginApUser;
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
public class RemoteClientUserFallbackFactory implements FallbackFactory<RemoteClientUserService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteClientUserFallbackFactory.class);

    @Override
    public RemoteClientUserService create(Throwable throwable)
    {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteClientUserService()
        {
            @Override
            public R<LoginApUser> getUserInfo(String username, String source)
            {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> registerUserInfo(ClientApUser sysUser, String source)
            {
                return R.fail("注册用户失败:" + throwable.getMessage());
            }
        };
    }
}
