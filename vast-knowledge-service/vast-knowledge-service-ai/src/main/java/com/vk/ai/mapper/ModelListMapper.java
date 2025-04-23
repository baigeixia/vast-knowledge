package com.vk.ai.mapper;

import com.mybatisflex.core.BaseMapper;
import com.vk.ai.domain.ModelList;
import org.apache.ibatis.annotations.Param;

/**
 *  映射层。
 *
 * @author 张三
 * @since 2025-03-31
 */
public interface ModelListMapper extends BaseMapper<ModelList> {

    void upTokenLimit(@Param("modelId")String modelId,@Param("usedTokens") int usedTokens);
}
