package com.vk.user.controller;

import com.mybatisflex.core.query.QueryWrapper;
import com.vk.common.core.domain.R;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.security.annotation.InnerAuth;
import com.vk.user.domain.*;
import com.vk.user.domain.dto.UserInfoLogin;
import com.vk.user.domain.vo.UserInfoVo;
import com.vk.user.mapper.ApUserInfoMapper;
import com.vk.user.mapper.ApUserMapper;
import com.vk.user.model.LoginApUser;
import com.vk.user.service.ApUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    @Autowired
    private ApUserMapper apUserMapper;


    @InnerAuth
    @GetMapping("/info/{username}")
    public R<LoginApUser> info(@PathVariable(name = "username") String username)
    {
        // ApUser apUser = apUserService.getOne(QueryWrapper.create().where(AP_USER.NAME.eq(username)));
        UserAndInfo userInfoVo = apUserMapper.getUserinfoByName(username);
        if (StringUtils.isNull(userInfoVo))
        {
            return R.fail("用户名或密码错误");
        }
        // 角色集合
        // Set<String> roles = permissionService.getRolePermission(sysUser);
        // 权限集合
        // Set<String> permissions = permissionService.getMenuPermission(sysUser);
        LoginApUser resultVo = new LoginApUser();
        ClientApUser user = new ClientApUser();
        BeanUtils.copyProperties(userInfoVo,user);
        resultVo.setUsername(userInfoVo.getName());
        resultVo.setClientApUser(user);
        return R.ok(resultVo);
    }



    @PostMapping("/getUserList")
    public R<Map<Long, AuthorInfo> > getUserList(@RequestBody Set<Long> userId)
    {
        Map<Long, AuthorInfo> result=apUserService.getUserList(userId);
        return R.ok(result);
    }
    @GetMapping("/upImage")
    R<Boolean> upImage(@RequestParam(name = "url") String url,@RequestParam(name = "userid") Long userid){
        Boolean result=apUserService.upImage(userid,url);
        return R.ok(result);
    }

}
