package com.vk.auth.controller.user;


import com.vk.auth.form.system.SystemLoginBody;
import com.vk.auth.form.user.UserLoginBody;
import com.vk.auth.service.user.UserLoginService;
import com.vk.common.core.domain.R;
import com.vk.common.core.web.domain.AjaxResult;
import com.vk.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/security")
public class UserSecurityController {

    @Autowired
    private UserLoginService userLoginService;

    @PostMapping("login")
    public AjaxResult login(@RequestBody UserLoginBody form)
    {
        // 用户登录
       Map<String, Object> userInfo = userLoginService.login(form.getEmail(), form.getPassword(),form.getWaitCode(),form.getCodeOrPas());
        // 获取登录token
        return AjaxResult.success(userInfo);
    }
}
