package com.vk.ai.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.vk.ai.domain.ChatInfo;
import com.vk.ai.domain.vo.AiMgListVo;
import com.vk.ai.domain.vo.ChatInfoVo;
import com.vk.ai.domain.vo.PageVo;
import com.vk.db.domain.aiMessage.AiMg;

/**
 * 消息详情 服务层。
 *
 * @author 张三
 * @since 2025-04-17
 */
public interface ChatInfoService extends IService<ChatInfo> {

    Page<ChatInfoVo> getUserList(Integer offset, Integer limit);

    PageVo<AiMgListVo> conversation(String id, Integer offset, Integer limit);
}
