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
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserSecurityController {

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("login")
    public AjaxResult login(@RequestBody UserLoginBody form, HttpServletResponse response)
    {
        // 用户登录
        LoginApUser userInfo = userLoginService.login(form.getToken(),form.getEmail(), form.getPassword(),form.getCodeOrPas());
        // 获取登录token
        return AjaxResult.success(tokenService.createToken(userInfo,response));
    }

    @DeleteMapping("logout")
    public R<?> logout(HttpServletRequest request,HttpServletResponse response)
    {
        String token = SecurityUtils.getRefreshToken(request);
        if (StringUtils.isNotEmpty(token))
        {
            Claims claims = TokenUtils.parseToken(token);
            if (null==claims){
                return R.ok();
            }
            // 删除用户缓存记录
            AuthUtil.logoutByToken(token);
            // AuthUtil.logoutRefreshToken(response);
        }
        return R.ok();
    }

   @PostMapping("refresh")
   public R<?> refresh(HttpServletRequest request, HttpServletResponse response)
   {
       LoginApUser loginApUser = tokenService.getLoginApUser(request);
       if (!ObjectUtils.isEmpty(loginApUser))
       {
           String key = loginApUser.getToken();
           String username = loginApUser.getUsername();
           Long userId = loginApUser.getClientApUser().getId();

           String token = tokenService.createToken(key, userId, username);
           // 刷新redis有效期
           tokenService.refreshTokenRedis(loginApUser);
           tokenService.refreshToken(response,key);
           return R.ok(token);
       }
       return R.fail(401,"请重新登录");
   }

}
