package com.vk.ai.template;


import com.vk.ai.domain.ModelList;
import com.vk.ai.domain.dto.GeneralMessageDto;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @version 1.0
 * @description 说明
 * @package com.vk.dfs.service
 */
public interface AiTemplate {

    /**
     * 发送消息
     *
     * @param message
     * @return
     */
    SseEmitter chatMessage(GeneralMessageDto message, ModelList modelList);


}