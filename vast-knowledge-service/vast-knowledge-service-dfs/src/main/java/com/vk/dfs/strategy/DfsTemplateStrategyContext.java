package com.vk.dfs.strategy;


import com.vk.dfs.enums.DFSType;
import com.vk.dfs.template.AbstractDfsTemplate;
import com.vk.dfs.template.DfsTemplate;
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
public class DfsTemplateStrategyContext implements ApplicationContextAware {

    //key  就是某一个类型 value 就是接口对应的具体子类对象
    private final Map<DFSType, DfsTemplate> dsfTemplates = new EnumMap<DFSType, DfsTemplate>(DFSType.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //抽闲类的所有的子类对象 从容器中获取所有的子类对象 key: beanName value bean对象
        Map<String, AbstractDfsTemplate> types = applicationContext.getBeansOfType(AbstractDfsTemplate.class);
        Map<DFSType, AbstractDfsTemplate> map = types.values().stream()
            .collect(Collectors.toMap(AbstractDfsTemplate::support, Function.identity()));
        //循环抽象类的子类
        dsfTemplates.putAll(map);
    }

    /**
     * 提供方法获取具体实现类
     * @param dfsType
     * @return
     */
    public DfsTemplate getTemplate(DFSType dfsType) {
        return dsfTemplates.get(dfsType);
    }

}