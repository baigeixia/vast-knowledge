package com.vk.user.controller;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.vk.common.core.domain.R;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.web.domain.AjaxResult;
import com.vk.common.log.annotation.Log;
import com.vk.common.log.enums.BusinessType;
import com.vk.common.security.annotation.RequiresPermissions;
import com.vk.user.domain.ApUser;
import com.vk.user.domain.AuthorInfo;
import com.vk.user.domain.vo.UserListVo;
import com.vk.user.mapper.ApUserMapper;
import com.vk.user.service.ApUserService;
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
    @RequiresPermissions("system:user:ban")
    @Log(title = "封禁或解封", businessType = BusinessType.UPDATE)
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
