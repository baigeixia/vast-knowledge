package com.vk.user.controller;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.vk.common.core.constant.SecurityConstants;
import com.vk.common.core.domain.R;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.web.domain.AjaxResult;
import com.vk.common.security.annotation.InnerAuth;

import com.vk.user.domain.ApUser;
import com.vk.user.domain.AuthorInfo;
import com.vk.user.domain.ClientApUser;
import com.vk.user.domain.vo.LocalUserInfoVo;
import com.vk.user.domain.vo.UserInfoVo;
import com.vk.user.model.LoginApUser;
import com.vk.user.service.ApUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.vk.user.domain.table.ApUserTableDef.AP_USER;

/**
 * APP用户信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/User")
public class ApUserController {

    @Autowired
    private ApUserService apUserService;

    @InnerAuth
    @GetMapping("/info/{username}")
    public R<LoginApUser> info(@PathVariable(name = "username") String username)
    {
        ApUser apUser = apUserService.getOne(QueryWrapper.create().where(AP_USER.NAME.eq(username)));
        if (StringUtils.isNull(apUser))
        {
            return R.fail("用户名或密码错误");
        }
        // 角色集合
        // Set<String> roles = permissionService.getRolePermission(sysUser);
        // 权限集合
        // Set<String> permissions = permissionService.getMenuPermission(sysUser);
        LoginApUser resultVo = new LoginApUser();
        ClientApUser user = new ClientApUser();
        BeanUtils.copyProperties(apUser,user);
        resultVo.setApUser(user);
        return R.ok(resultVo);
    }



    @PostMapping("/getUserList")
    public R<Map<Long, AuthorInfo> > getUserList(@RequestBody Set<Long> userId)
    {
        Map<Long, AuthorInfo> result=apUserService.getUserList(userId);
        return R.ok(result);
    }


    @GetMapping("/getInfo")
    public AjaxResult getInfo(
            @RequestParam(name = "id") Long id
    ){
        UserInfoVo result=apUserService.getInfo(id);
        return AjaxResult.success(result);
    }


    @GetMapping("/getLocalInfo")
    public AjaxResult getLocalInfo( ){
        LocalUserInfoVo result=apUserService.getLocalInfo();
        return AjaxResult.success(result);
    }


}
