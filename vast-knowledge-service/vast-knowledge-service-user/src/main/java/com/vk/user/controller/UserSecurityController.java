package com.vk.user.controller;


import com.vk.common.core.domain.R;
import com.vk.common.core.enums.UserType;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.utils.TokenUtils;
import com.vk.common.core.web.domain.AjaxResult;
import com.vk.common.security.auth.AuthUtil;
import com.vk.common.security.service.TokenService;
import com.vk.common.security.utils.SecurityUtils;
import com.vk.system.model.LoginUser;
import com.vk.user.domain.ClientApUser;
import com.vk.user.domain.user.UserLoginBody;
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
    public AjaxResult login(@RequestBody UserLoginBody form,HttpServletRequest request, HttpServletResponse response)
    {
        // 用户登录
        LoginUser<ClientApUser> login = userLoginService.login(form.getToken(), form.getEmail(), form.getPassword(), form.getCodeOrPas());
        // 获取登录token
        return AjaxResult.success(tokenService.createToken(login,request,response));
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
        }
        return R.ok();
    }

   @PostMapping("refresh")
   public R<?> refresh(HttpServletRequest request, HttpServletResponse response)
   {
       String userType = UserType.USER_TYPE.getType();
       LoginUser<ClientApUser> loginUser = tokenService.getLoginUser(request,userType);
       if (!ObjectUtils.isEmpty(loginUser))
       {
           String key = loginUser.getToken();
           String username = loginUser.getUsername();
           Long userId = loginUser.getSysUser().getId();

           String token = tokenService.createToken(key, userId, username,userType);
           // 刷新redis有效期
           tokenService.refreshTokenRedis(loginUser);
           tokenService.addRefreshToken(request,response,userType,key);
           return R.ok(token);
       }
       return R.fail(401,"请重新登录");
   }

}
