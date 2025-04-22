package com.vk.ai.controller;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.vk.ai.domain.ChatInfo;
import com.vk.ai.domain.vo.AiMgListVo;
import com.vk.ai.domain.vo.ChatInfoVo;
import com.vk.ai.domain.vo.PageVo;
import com.vk.ai.service.ChatInfoService;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.web.domain.AjaxResult;
import com.vk.db.domain.aiMessage.AiMg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

import static com.vk.ai.domain.table.ChatInfoTableDef.CHAT_INFO;

/**
 * 消息详情 控制层。
 *
 * @author 张三
 * @since 2025-04-17
 */
@RestController
@RequestMapping("/chatInfo")
public class ChatInfoController {

    @Autowired
    private ChatInfoService chatInfoService;

    /**
     * 查询所有消息详情。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public AjaxResult getUserList(
            @RequestParam(name = "offset",defaultValue = "1") Integer offset,
            @RequestParam(name = "limit",defaultValue = "5") Integer limit
    ) {
        int start = (offset - 1) * limit;
        Page<ChatInfoVo> vo= chatInfoService.getUserList(start,limit);
        return AjaxResult.success(vo);
    }

    /**
     * 对话详情。
     *
     * @return
     */
    @GetMapping("/conversation/{id}")
    public AjaxResult conversation(
            @PathVariable(name = "id")String id,
            @RequestParam(name = "offset",defaultValue = "1") Integer offset,
            @RequestParam(name = "limit",defaultValue = "5") Integer limit
    ) {
        int start = (offset - 1) * limit;
        PageVo<AiMgListVo> conversation = chatInfoService.conversation(id, start, limit);
        return AjaxResult.success(conversation);
    }

}
