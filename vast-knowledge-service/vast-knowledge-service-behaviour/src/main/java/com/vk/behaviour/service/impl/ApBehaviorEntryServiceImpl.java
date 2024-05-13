package com.vk.behaviour.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.behaviour.domain.ApBehaviorEntry;
import com.vk.behaviour.mapper.ApBehaviorEntryMapper;
import com.vk.behaviour.service.ApBehaviorEntryService;
import org.springframework.stereotype.Service;

/**
 * APP行为实体,一个行为实体可能是用户或者设备，或者其它 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class ApBehaviorEntryServiceImpl extends ServiceImpl<ApBehaviorEntryMapper, ApBehaviorEntry> implements ApBehaviorEntryService {

}
