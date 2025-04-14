package com.vk.ai.strategy;


import com.vk.ai.enums.AiType;
import com.vk.ai.template.AbstractAiTemplate;
import com.vk.ai.template.AiTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @description 说明 ApplicationContextAware接口：spring容器完成后，再通知
 */
@Component
public class AiTemplateStrategyContext implements ApplicationContextAware {

    //key  就是某一个类型 value 就是接口对应的具体子类对象
    private final Map<AiType, AiTemplate> dsfTemplates = new EnumMap<>(AiType.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //抽象类的所有的子类对象 从容器中获取所有的子类对象 key: beanName value bean对象
        Map<String, AbstractAiTemplate> types = applicationContext.getBeansOfType(AbstractAiTemplate.class);
        Map<AiType, AbstractAiTemplate> map = types.values().stream()
            .collect(Collectors.toMap(AbstractAiTemplate::support, Function.identity()));
        //循环抽象类的子类
        dsfTemplates.putAll(map);
    }

    /**
     * 提供方法获取具体实现类
     * @param aiType
     * @return
     */
    public AiTemplate getTemplate(AiType aiType) {
        return dsfTemplates.get(aiType);
    }

}