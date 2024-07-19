package com.vk.search.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.search.domain.ApAssociateWords;
import com.vk.search.service.ApAssociateWordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 联想词 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/AssociateWords")
public class ApAssociateWordsController {

    @Autowired
    private ApAssociateWordsService apAssociateWordsService;

    /**
     * 添加联想词。
     *
     * @param apAssociateWords 联想词
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApAssociateWords apAssociateWords) {
        return apAssociateWordsService.save(apAssociateWords);
    }

    /**
     * 根据主键删除联想词。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apAssociateWordsService.removeById(id);
    }

    /**
     * 根据主键更新联想词。
     *
     * @param apAssociateWords 联想词
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApAssociateWords apAssociateWords) {
        return apAssociateWordsService.updateById(apAssociateWords);
    }

    /**
     * 查询所有联想词。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApAssociateWords> list() {
        return apAssociateWordsService.list();
    }

    /**
     * 根据联想词主键获取详细信息。
     *
     * @param id 联想词主键
     * @return 联想词详情
     */
    @GetMapping("getInfo/{id}")
    public ApAssociateWords getInfo(@PathVariable Serializable id) {
        return apAssociateWordsService.getById(id);
    }

    /**
     * 分页查询联想词。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApAssociateWords> page(Page<ApAssociateWords> page) {
        return apAssociateWordsService.page(page);
    }

}
