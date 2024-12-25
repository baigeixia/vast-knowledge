package com.vk.user.controller;



import com.vk.common.core.constant.SecurityConstants;
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
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
// @RequestMapping("")
public class UserSecurityController {

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("login")
    public AjaxResult login(@RequestBody UserLoginBody form, HttpServletResponse response)
    {
        // 用户登录
        LoginApUser userInfo = userLoginService.login(form.getEmail(), form.getPassword(),form.getWaitCode(),form.getCodeOrPas());
        // 获取登录token
        return AjaxResult.success(tokenService.createToken(userInfo,response));
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

   @PostMapping("refresh")
   public R<?> refresh(HttpServletRequest request, HttpServletResponse response)
   {
       LoginApUser loginApUser = tokenService.getLoginApUser(request);
       if (!ObjectUtils.isEmpty(loginApUser))
       {
           String token = loginApUser.getToken();
           String username = loginApUser.getUsername();
           Long userId = loginApUser.getClientApUser().getId();

           Map<String, Object> claimsMap = new HashMap<>();
           claimsMap.put(SecurityConstants.USER_KEY, token);
           claimsMap.put(SecurityConstants.DETAILS_USER_ID, userId);
           claimsMap.put(SecurityConstants.DETAILS_USERNAME, username);

           String serviceToken = TokenUtils.createToken(claimsMap);
           // 刷新redis有效期
           tokenService.refreshTokenRedis(loginApUser);
           tokenService.refreshToken(response,loginApUser.getToken());
           return R.ok(serviceToken);
       }
       return R.fail(401,"请重新登录");
   }

}
