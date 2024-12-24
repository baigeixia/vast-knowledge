package com.vk.user.controller;



import com.vk.common.core.domain.R;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.utils.TokenUtils;
import com.vk.common.core.web.domain.AjaxResult;
import com.vk.common.security.auth.AuthUtil;
import com.vk.common.security.service.TokenService;
import com.vk.common.security.utils.SecurityUtils;
import com.vk.user.domain.user.UserLoginBody;
import com.vk.user.model.LoginApUser;
import com.vk.user.service.user.UserLoginService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
// @RequestMapping("")
public class UserSecurityController {

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("login")
    public AjaxResult login(@RequestBody UserLoginBody form)
    {
        // 用户登录
        LoginApUser userInfo = userLoginService.login(form.getEmail(), form.getPassword(),form.getWaitCode(),form.getCodeOrPas());
        // 获取登录token
        return AjaxResult.success(tokenService.createToken(userInfo));
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

//    @PostMapping("refresh")
//    public R<?> refresh(HttpServletRequest request)
//    {
//        LoginApUser loginApUser = clientTokenService.getLoginApUser(request);
//        if (StringUtils.isNotNull(loginApUser))
//        {
//            // 刷新令牌有效期
//            clientTokenService.refreshToken(loginApUser);
//            return R.ok();
//        }
//        return R.ok();
//    }
//
//    @PostMapping("register")
//    public R<?> register(@RequestBody SystemRegisterBody registerBody)
//    {
//        // 用户注册
//        sysLoginService.register(registerBody.getUsername(), registerBody.getPassword());
//        return R.ok();
//    }
}
