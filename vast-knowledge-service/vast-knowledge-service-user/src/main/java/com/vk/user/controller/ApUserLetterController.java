package com.vk.user.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.user.domain.ApUserLetter;
import com.vk.user.service.ApUserLetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP用户私信信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/apUserLetter")
public class ApUserLetterController {

    @Autowired
    private ApUserLetterService apUserLetterService;

    /**
     * 添加APP用户私信信息。
     *
     * @param apUserLetter APP用户私信信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApUserLetter apUserLetter) {
        return apUserLetterService.save(apUserLetter);
    }

    /**
     * 根据主键删除APP用户私信信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apUserLetterService.removeById(id);
    }

    /**
     * 根据主键更新APP用户私信信息。
     *
     * @param apUserLetter APP用户私信信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApUserLetter apUserLetter) {
        return apUserLetterService.updateById(apUserLetter);
    }

    /**
     * 查询所有APP用户私信信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApUserLetter> list() {
        return apUserLetterService.list();
    }

    /**
     * 根据APP用户私信信息主键获取详细信息。
     *
     * @param id APP用户私信信息主键
     * @return APP用户私信信息详情
     */
    @GetMapping("getInfo/{id}")
    public ApUserLetter getInfo(@PathVariable Serializable id) {
        return apUserLetterService.getById(id);
    }

    /**
     * 分页查询APP用户私信信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApUserLetter> page(Page<ApUserLetter> page) {
        return apUserLetterService.page(page);
    }

}
