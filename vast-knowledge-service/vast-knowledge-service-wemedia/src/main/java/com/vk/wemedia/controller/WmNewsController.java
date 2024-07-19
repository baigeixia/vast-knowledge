package com.vk.wemedia.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.common.core.web.domain.AjaxResult;
import com.vk.wemedia.domain.WmNews;
import com.vk.wemedia.service.WmNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 自媒体图文内容信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/News")
public class WmNewsController {

    @Autowired
    private WmNewsService wmNewsService;

    /**
     * 添加自媒体图文内容信息。
     *
     * @param wmNews 自媒体图文内容信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody WmNews wmNews) {
        return wmNewsService.save(wmNews);
    }

    /**
     * 根据主键删除自媒体图文内容信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return wmNewsService.removeById(id);
    }

    /**
     * 根据主键更新自媒体图文内容信息。
     *
     * @param wmNews 自媒体图文内容信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody WmNews wmNews) {
        return wmNewsService.updateById(wmNews);
    }

    /**
     * 查询所有自媒体图文内容信息。
     *
     * @return 所有数据
     */
    @PostMapping("list")
    public AjaxResult getlist(@RequestBody WmNews news) {
        Page<WmNews> list=wmNewsService.getlist(news);
        return AjaxResult.success(list);
    }

    /**
     * 根据自媒体图文内容信息主键获取详细信息。
     *
     * @param id 自媒体图文内容信息主键
     * @return 自媒体图文内容信息详情
     */
    @GetMapping("getInfo/{id}")
    public WmNews getInfo(@PathVariable Serializable id) {
        return wmNewsService.getById(id);
    }

    /**
     * 分页查询自媒体图文内容信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<WmNews> page(Page<WmNews> page) {
        return wmNewsService.page(page);
    }

}
