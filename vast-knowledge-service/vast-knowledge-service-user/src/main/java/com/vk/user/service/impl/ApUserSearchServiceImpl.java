package com.vk.user.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.user.domain.ApUserSearch;
import com.vk.user.mapper.ApUserSearchMapper;
import com.vk.user.service.ApUserSearchService;
import org.springframework.stereotype.Service;

/**
 * APP用户搜索信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class ApUserSearchServiceImpl extends ServiceImpl<ApUserSearchMapper, ApUserSearch> implements ApUserSearchService {

}
