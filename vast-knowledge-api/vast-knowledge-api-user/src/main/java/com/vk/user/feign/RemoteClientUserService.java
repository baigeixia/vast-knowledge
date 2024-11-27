package com.vk.user.feign;

import com.vk.common.core.constant.SecurityConstants;
import com.vk.common.core.constant.ServiceNameConstants;
import com.vk.common.core.domain.R;
import com.vk.user.domain.AuthorInfo;
import com.vk.user.domain.ClientApUser;
import com.vk.user.factory.RemoteClientUserFallbackFactory;
import com.vk.user.model.LoginApUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@FeignClient(contextId = "remoteClientUserService", value = ServiceNameConstants.USER_SERVICE, fallbackFactory = RemoteClientUserFallbackFactory.class)
public interface RemoteClientUserService {

    @GetMapping("/User/info/{username}")
    R<LoginApUser> getUserInfo(@PathVariable(name = "username") String username, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @PostMapping("/User/register")
    R<Boolean> registerUserInfo(@RequestBody ClientApUser clientApUser, @RequestHeader(SecurityConstants.FROM_SOURCE)  String source);

    @PostMapping("/User/getUserList")
    R<Map<Long, AuthorInfo>> getUserList(@RequestBody Set<Long> userId);

    @GetMapping("/User/upImage")
    R<Boolean> upImage(@RequestParam(name = "url") String url,@RequestParam(name = "userid") Long userid);

}
