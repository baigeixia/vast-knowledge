package com.vk.common.security.interceptor;


import com.vk.common.core.constant.SecurityConstants;
import com.vk.common.core.context.SecurityContextHolder;
import com.vk.common.core.utils.ServletUtils;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.utils.TokenUtils;
import com.vk.common.security.auth.AuthUtil;
import com.vk.common.security.utils.SecurityUtils;
import com.vk.system.model.LoginUser;
import com.vk.user.model.LoginApUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

/**
 * 自定义请求头拦截器，将Header数据封装到线程变量中方便获取
 * 注意：此拦截器会同时验证当前用户有效期自动刷新有效期
 *
 * @author vk
 */
public class HeaderInterceptor implements AsyncHandlerInterceptor
{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    {
        if (!(handler instanceof HandlerMethod))
        {
            return true;
        }
        SecurityContextHolder.setUserId(ServletUtils.getHeader(request, SecurityConstants.DETAILS_USER_ID));
        SecurityContextHolder.setUserName(ServletUtils.getHeader(request, SecurityConstants.DETAILS_USERNAME));
        SecurityContextHolder.setUserKey(ServletUtils.getHeader(request, SecurityConstants.USER_KEY));
        SecurityContextHolder.set(SecurityConstants.ADMIN_OPEN, Boolean.parseBoolean(ServletUtils.getHeader(request, SecurityConstants.ADMIN_OPEN)));

        String token = SecurityUtils.getToken();
        if (StringUtils.isNotEmpty(token))
        {
            if (SecurityUtils.verificationAdmin(request)){
                LoginUser loginApUser = AuthUtil.getLoginUser(token);
                contextSet(loginApUser);
            }else {
                LoginApUser loginApUser = AuthUtil.getLoginApUser(token);
                contextSet(loginApUser);
            }
        }
        return true;
    }

    private void contextSet(LoginUser userinfo){
        if (!ObjectUtils.isEmpty(userinfo))
        {
            AuthUtil.verifyLoginUserExpire(userinfo);
            SecurityContextHolder.set(SecurityConstants.LOGIN_ADMIN,userinfo);
        }
    }

    private void contextSet(LoginApUser userinfo){
        if (!ObjectUtils.isEmpty(userinfo))
        {
            AuthUtil.verifyLoginUserExpire(userinfo);
           SecurityContextHolder.set(SecurityConstants.LOGIN_USER, userinfo);
        }
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception
    {
        SecurityContextHolder.remove();
    }
}
