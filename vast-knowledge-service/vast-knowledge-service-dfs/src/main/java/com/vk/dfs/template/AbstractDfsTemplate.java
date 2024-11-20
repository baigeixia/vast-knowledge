package com.vk.dfs.template;

import com.itheima.dfs.enums.DFSType;
import org.springframework.beans.factory.InitializingBean;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * @version 1.0
 * @description 说明
 * @package com.itheima.dfs.template
 */
public abstract class AbstractDfsTemplate implements DfsTemplate, InitializingBean {

    /**
     * 定义支持的类型 必须设置值
     *
     * @return
     */
    public abstract DFSType support();

    @Override
    public void afterPropertiesSet() throws Exception {
        // 支持文件存储方案
        DFSType[] values = DFSType.values();
        // 现有提供文件存储模板，模板必是DFSType中的一种实现，如果不是则报错
        DFSType support = support();

        if(!Arrays.stream(values).anyMatch(Predicate.isEqual(support))){
            throw new RuntimeException("不支持的dfs类型");
        }
    }
}