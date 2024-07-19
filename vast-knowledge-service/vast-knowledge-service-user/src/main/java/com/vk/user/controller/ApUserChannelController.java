package com.vk.user.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.user.domain.ApUserChannel;
import com.vk.user.service.ApUserChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP用户频道信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/UserChannel")
public class ApUserChannelController {

    @Autowired
    private ApUserChannelService apUserChannelService;

    /**
     * 添加APP用户频道信息。
     *
     * @param apUserChannel APP用户频道信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApUserChannel apUserChannel) {
        return apUserChannelService.save(apUserChannel);
    }

    /**
     * 根据主键删除APP用户频道信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apUserChannelService.removeById(id);
    }

    /**
     * 根据主键更新APP用户频道信息。
     *
     * @param apUserChannel APP用户频道信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApUserChannel apUserChannel) {
        return apUserChannelService.updateById(apUserChannel);
    }

    /**
     * 查询所有APP用户频道信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApUserChannel> list() {
        return apUserChannelService.list();
    }

    /**
     * 根据APP用户频道信息主键获取详细信息。
     *
     * @param id APP用户频道信息主键
     * @return APP用户频道信息详情
     */
    @GetMapping("getInfo/{id}")
    public ApUserChannel getInfo(@PathVariable Serializable id) {
        return apUserChannelService.getById(id);
    }

    /**
     * 分页查询APP用户频道信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApUserChannel> page(Page<ApUserChannel> page) {
        return apUserChannelService.page(page);
    }

}
