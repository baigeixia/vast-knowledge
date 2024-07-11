package com.vk.article.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.article.domain.ApArticleConfig;
import com.vk.article.service.ApArticleConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP已发布文章配置 控制层。
 *
 * @author 张三
 * @since 2024-07-11
 */
@RestController
@RequestMapping("/apArticleConfig")
public class ApArticleConfigController {

    @Autowired
    private ApArticleConfigService apArticleConfigService;

    /**
     * 添加APP已发布文章配置。
     *
     * @param apArticleConfig APP已发布文章配置
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApArticleConfig apArticleConfig) {
        return apArticleConfigService.save(apArticleConfig);
    }

    /**
     * 根据主键删除APP已发布文章配置。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apArticleConfigService.removeById(id);
    }

    /**
     * 根据主键更新APP已发布文章配置。
     *
     * @param apArticleConfig APP已发布文章配置
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApArticleConfig apArticleConfig) {
        return apArticleConfigService.updateById(apArticleConfig);
    }

    /**
     * 查询所有APP已发布文章配置。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApArticleConfig> list() {
        return apArticleConfigService.list();
    }

    /**
     * 根据APP已发布文章配置主键获取详细信息。
     *
     * @param id APP已发布文章配置主键
     * @return APP已发布文章配置详情
     */
    @GetMapping("getInfo/{id}")
    public ApArticleConfig getInfo(@PathVariable Serializable id) {
        return apArticleConfigService.getById(id);
    }

    /**
     * 分页查询APP已发布文章配置。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApArticleConfig> page(Page<ApArticleConfig> page) {
        return apArticleConfigService.page(page);
    }

}
