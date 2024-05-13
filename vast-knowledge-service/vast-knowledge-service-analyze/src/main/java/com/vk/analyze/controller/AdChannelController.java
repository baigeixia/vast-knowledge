package com.vk.analyze.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.analyze.domain.AdChannel;
import com.vk.analyze.service.AdChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 频道信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/adChannel")
public class AdChannelController {

    @Autowired
    private AdChannelService adChannelService;

    /**
     * 添加频道信息。
     *
     * @param adChannel 频道信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody AdChannel adChannel) {
        return adChannelService.save(adChannel);
    }

    /**
     * 根据主键删除频道信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return adChannelService.removeById(id);
    }

    /**
     * 根据主键更新频道信息。
     *
     * @param adChannel 频道信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody AdChannel adChannel) {
        return adChannelService.updateById(adChannel);
    }

    /**
     * 查询所有频道信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<AdChannel> list() {
        return adChannelService.list();
    }

    /**
     * 根据频道信息主键获取详细信息。
     *
     * @param id 频道信息主键
     * @return 频道信息详情
     */
    @GetMapping("getInfo/{id}")
    public AdChannel getInfo(@PathVariable Serializable id) {
        return adChannelService.getById(id);
    }

    /**
     * 分页查询频道信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<AdChannel> page(Page<AdChannel> page) {
        return adChannelService.page(page);
    }

}
