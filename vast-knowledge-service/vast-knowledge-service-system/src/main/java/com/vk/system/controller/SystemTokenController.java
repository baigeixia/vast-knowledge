package com.vk.system.controller;


import com.vk.common.core.domain.R;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.utils.TokenUtils;
import com.vk.common.security.auth.AuthUtil;
import com.vk.common.security.service.TokenService;
import com.vk.common.security.utils.SecurityUtils;
import com.vk.system.domain.system.SystemLoginBody;
import com.vk.system.domain.system.SystemRegisterBody;
import com.vk.system.model.LoginUser;
import com.vk.system.service.system.SysLoginService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * token 控制
 * 
 * @author vk
 */
@RestController
public class SystemTokenController
{
    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginService sysLoginService;

    @PostMapping("login")
    public R<?> login(@RequestBody SystemLoginBody form, HttpServletResponse response)
    {
        // 用户登录
        LoginUser userInfo = sysLoginService.login(form.getUsername(), form.getPassword());
        // 获取登录token
        return R.ok(tokenService.createToken(userInfo,response));
    }

    @DeleteMapping("logout")
    public R<?> logout(HttpServletRequest request)
    {
        String token = SecurityUtils.getRefreshToken(request);
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
            sysLoginService.logout(username);
        }
        return R.ok();
    }

    @PostMapping("refresh")
    public R<?> refresh(HttpServletRequest request, HttpServletResponse response)
    {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser))
        {
            String key = loginUser.getToken();
            String username = loginUser.getUsername();
            Long userId = loginUser.getSysUser().getUserId();

            String token = tokenService.createToken(key, userId, username);
            // 刷新令牌有效期
            tokenService.refreshTokenRedis(loginUser);
            tokenService.refreshToken(response,key);
            return R.ok(token);
        }
        return R.ok();
    }

    @PostMapping("register")
    public R<?> register(@RequestBody SystemRegisterBody registerBody)
    {
        // 用户注册
        sysLoginService.register(registerBody.getUsername(), registerBody.getPassword());
        return R.ok();
    }
}
