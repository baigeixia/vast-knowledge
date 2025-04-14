package com.vk.ai.template;

import com.mybatisflex.core.BaseMapper;
import com.vk.ai.enums.AiType;
import org.springframework.beans.factory.InitializingBean;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * @version 1.0
 * @description 说明
 * @package com.itheima.dfs.template
 */
public abstract class AbstractAiTemplate<M extends BaseMapper<T>, T> implements AiTemplate, InitializingBean {

    /**
     * 定义支持的类型 必须设置值
     *
     * @return
     */
    public abstract AiType support();


    @Override
    public void afterPropertiesSet() {
        // 支持文件存储方案
        AiType[] values = AiType.values();
        // 现有提供文件存储模板，模板必是DFSType中的一种实现，如果不是则报错
        AiType support = support();

        if(Arrays.stream(values).noneMatch(Predicate.isEqual(support))){
            throw new RuntimeException("不支持的Ai type");
        }
    }
}