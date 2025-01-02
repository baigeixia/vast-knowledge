package com.vk.user.controller;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.vk.common.core.domain.R;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.web.domain.AjaxResult;
import com.vk.common.security.annotation.InnerAuth;
import com.vk.user.domain.ApUser;
import com.vk.user.domain.AuthorInfo;
import com.vk.user.domain.ClientApUser;
import com.vk.user.domain.UserAndInfo;
import com.vk.user.domain.vo.UserListVo;
import com.vk.user.mapper.ApUserMapper;
import com.vk.user.model.LoginApUser;
import com.vk.user.service.ApUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

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
    public R<LoginApUser> info(@PathVariable(name = "username") String username) {
        // ApUser apUser = apUserService.getOne(QueryWrapper.create().where(AP_USER.NAME.eq(username)));
        UserAndInfo userInfoVo = apUserMapper.getUserinfoByName(username);
        if (StringUtils.isNull(userInfoVo)) {
            return R.fail("用户名或密码错误");
        }
        // 角色集合
        // Set<String> roles = permissionService.getRolePermission(sysUser);
        // 权限集合
        // Set<String> permissions = permissionService.getMenuPermission(sysUser);
        LoginApUser resultVo = new LoginApUser();
        ClientApUser user = new ClientApUser();
        BeanUtils.copyProperties(userInfoVo, user);
        resultVo.setUsername(userInfoVo.getName());
        resultVo.setClientApUser(user);
        return R.ok(resultVo);
    }


    @PostMapping("/getUserList")
    public R<Map<Long, AuthorInfo>> getUserList(@RequestBody Set<Long> userId) {
        Map<Long, AuthorInfo> result = apUserService.getUserList(userId);
        return R.ok(result);
    }

    @GetMapping("/upImage")
    R<Boolean> upImage(@RequestParam(name = "url") String url, @RequestParam(name = "userid") Long userid) {
        Boolean result = apUserService.upImage(userid, url);
        return R.ok(result);
    }

    @GetMapping("/getUserList")
    public AjaxResult getUserList(
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "status", required = false) Boolean status,
            @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize
    ) {
        Page<UserListVo> listVoPage = apUserService.pageAs(Page.of(pageNum, pageSize)
                , QueryWrapper.create().where(AP_USER.EMAIL.like(email, StringUtils.isNotEmpty(email))).and(AP_USER.STATUS.eq(status,status!=null))
                , UserListVo.class);
        return AjaxResult.success(listVoPage);
    }

    /**
     * 封禁
     * @param id   用户id
     * @param type 0:正常 1:锁定
     * @return
     */
    @DeleteMapping("/userBan")
    public AjaxResult userBan(
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "type") Boolean type
    ) {
        ApUser user = apUserService.getById(id);
        if (ObjectUtils.isEmpty(user)) {
            throw new LeadNewsException("错误的用户");
        }
        Boolean status = user.getStatus();
        if (status.equals(type)) {
            throw new LeadNewsException("用户已是" + (status ? "封禁" : "正常") + "状态");
        }

        ApUser upUser = new ApUser();
        upUser.setId(id);
        upUser.setStatus(!status);
        apUserService.updateById(upUser);

        return AjaxResult.success();
    }

}
