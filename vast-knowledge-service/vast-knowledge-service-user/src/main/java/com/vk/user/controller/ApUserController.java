package com.vk.user.controller;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.vk.common.core.constant.SecurityConstants;
import com.vk.common.core.domain.R;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.security.annotation.InnerAuth;

import com.vk.user.domain.ApUser;
import com.vk.user.domain.AuthorInfo;
import com.vk.user.domain.ClientApUser;
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



    @PostMapping("/User/getUserList")
    public R<List<Map<Long, AuthorInfo>> > getUserList(@RequestBody Set<Long> userId)
    {
        List<Map<Long, AuthorInfo>>  result=apUserService.getUserList(userId);
        return R.ok(result);
    }




    /**
     * 添加APP用户信息。
     *
     * @param apUser APP用户信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApUser apUser) {
        return apUserService.save(apUser);
    }

    /**
     * 根据主键删除APP用户信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apUserService.removeById(id);
    }

    /**
     * 根据主键更新APP用户信息。
     *
     * @param apUser APP用户信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApUser apUser) {
        return apUserService.updateById(apUser);
    }

    /**
     * 查询所有APP用户信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApUser> list() {
        return apUserService.list();
    }

    /**
     * 根据APP用户信息主键获取详细信息。
     *
     * @param id APP用户信息主键
     * @return APP用户信息详情
     */
    @GetMapping("getInfo/{id}")
    public ApUser getInfo(@PathVariable Serializable id) {
        return apUserService.getById(id);
    }

    /**
     * 分页查询APP用户信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApUser> page(Page<ApUser> page) {
        return apUserService.page(page);
    }

}
