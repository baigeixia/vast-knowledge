package com.vk.ai.template;


import com.vk.ai.domain.GeneralMessage;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

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
     * @return 文件的路径信息
     */
    String chatMessage(GeneralMessage message);


}