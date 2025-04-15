package com.vk.ai.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.ai.domain.ModelList;
import com.vk.ai.mapper.ModelListMapper;
import com.vk.ai.service.ModelListService;
import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author 张三
 * @since 2025-04-15
 */
@Service
public class ModelListServiceImpl extends ServiceImpl<ModelListMapper, ModelList> implements ModelListService {

}
