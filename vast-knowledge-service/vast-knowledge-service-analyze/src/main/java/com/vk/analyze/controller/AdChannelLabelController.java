package com.vk.analyze.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.analyze.domain.AdChannelLabel;
import com.vk.analyze.service.AdChannelLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 频道标签信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/ChannelLabel")
public class AdChannelLabelController {

    @Autowired
    private AdChannelLabelService adChannelLabelService;

    /**
     * 添加频道标签信息。
     *
     * @param adChannelLabel 频道标签信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody AdChannelLabel adChannelLabel) {
        return adChannelLabelService.save(adChannelLabel);
    }

    /**
     * 根据主键删除频道标签信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return adChannelLabelService.removeById(id);
    }

    /**
     * 根据主键更新频道标签信息。
     *
     * @param adChannelLabel 频道标签信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody AdChannelLabel adChannelLabel) {
        return adChannelLabelService.updateById(adChannelLabel);
    }

    /**
     * 查询所有频道标签信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<AdChannelLabel> list() {
        return adChannelLabelService.list();
    }

    /**
     * 根据频道标签信息主键获取详细信息。
     *
     * @param id 频道标签信息主键
     * @return 频道标签信息详情
     */
    @GetMapping("getInfo/{id}")
    public AdChannelLabel getInfo(@PathVariable Serializable id) {
        return adChannelLabelService.getById(id);
    }

    /**
     * 分页查询频道标签信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<AdChannelLabel> page(Page<AdChannelLabel> page) {
        return adChannelLabelService.page(page);
    }

}
