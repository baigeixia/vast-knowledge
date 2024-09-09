package com.vk.behaviour.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.behaviour.domain.ApReadBehavior;
import com.vk.behaviour.service.ApReadBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP阅读行为 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/ReadBehavior")
public class ApReadBehaviorController {

    @Autowired
    private ApReadBehaviorService apReadBehaviorService;


    /**
     * 根据主键删除APP阅读行为。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apReadBehaviorService.removeById(id);
    }



    /**
     * 查询所有APP阅读行为。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApReadBehavior> list() {
        return apReadBehaviorService.list();
    }


}
