package com.vk.user.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.user.domain.ApUserMessage;
import com.vk.user.service.ApUserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP用户消息通知信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/apUserMessage")
public class ApUserMessageController {

    @Autowired
    private ApUserMessageService apUserMessageService;

    /**
     * 添加APP用户消息通知信息。
     *
     * @param apUserMessage APP用户消息通知信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApUserMessage apUserMessage) {
        return apUserMessageService.save(apUserMessage);
    }

    /**
     * 根据主键删除APP用户消息通知信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apUserMessageService.removeById(id);
    }

    /**
     * 根据主键更新APP用户消息通知信息。
     *
     * @param apUserMessage APP用户消息通知信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApUserMessage apUserMessage) {
        return apUserMessageService.updateById(apUserMessage);
    }

    /**
     * 查询所有APP用户消息通知信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApUserMessage> list() {
        return apUserMessageService.list();
    }

    /**
     * 根据APP用户消息通知信息主键获取详细信息。
     *
     * @param id APP用户消息通知信息主键
     * @return APP用户消息通知信息详情
     */
    @GetMapping("getInfo/{id}")
    public ApUserMessage getInfo(@PathVariable Serializable id) {
        return apUserMessageService.getById(id);
    }

    /**
     * 分页查询APP用户消息通知信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApUserMessage> page(Page<ApUserMessage> page) {
        return apUserMessageService.page(page);
    }

}
