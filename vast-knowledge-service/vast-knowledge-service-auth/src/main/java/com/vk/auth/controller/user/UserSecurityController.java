package com.vk.auth.controller.user;


import com.vk.auth.form.system.SystemLoginBody;
import com.vk.auth.form.user.UserLoginBody;
import com.vk.auth.service.system.SysLoginService;
import com.vk.auth.service.user.UserLoginService;
import com.vk.common.core.domain.R;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.utils.TokenUtils;
import com.vk.common.core.web.domain.AjaxResult;
import com.vk.common.security.auth.AuthUtil;
import com.vk.common.security.service.ClientTokenService;
import com.vk.common.security.service.TokenService;
import com.vk.common.security.utils.SecurityUtils;
import com.vk.system.api.model.LoginUser;
import com.vk.user.feign.model.LoginApUser;
import feign.Client;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
    @RequestMapping("/security")
public class UserSecurityController {

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private ClientTokenService clientTokenService;


    @Autowired
    private SysLoginService sysLoginService;

    @PostMapping("login")
    public AjaxResult login(@RequestBody UserLoginBody form)
    {
        // 用户登录
        LoginApUser userInfo = userLoginService.login(form.getEmail(), form.getPassword(),form.getWaitCode(),form.getCodeOrPas());
        // 获取登录token
        return AjaxResult.success(clientTokenService.createToken(userInfo));
    }

    @DeleteMapping("logout")
    public R<?> logout(HttpServletRequest request)
    {
        String token = SecurityUtils.getToken(request);
        if (StringUtils.isNotEmpty(token))
        {
            Claims claims = TokenUtils.parseToken(token);
            if (null==claims){
                return R.ok();
            }
            String username = TokenUtils.getUserName(token);
            // 删除用户缓存记录
            AuthUtil.logoutByToken(token);
            // 记录用户退出日志
            // sysLoginService.logout(username);
        }
        return R.ok();
    }
}
