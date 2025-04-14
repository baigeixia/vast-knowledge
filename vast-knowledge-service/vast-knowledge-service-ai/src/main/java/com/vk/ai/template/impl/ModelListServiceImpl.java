package com.vk.ai.template.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.ai.domain.GeneralMessage;
import com.vk.ai.domain.ModelList;
import com.vk.ai.enums.AiType;
import com.vk.ai.mapper.ModelListMapper;
import com.vk.ai.template.AbstractAiTemplate;
import com.vk.ai.template.AiTemplate;
import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author 张三
 * @since 2025-03-31
 */
@Service
public class ModelListServiceImpl extends AbstractAiTemplate<ModelListMapper, ModelList> {

    @Override
    public AiType support() {
        return null;
    }


    @Override
    public String chatMessage(GeneralMessage message) {
        return null;
    }
}
