package com.vk.user.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.user.domain.ApUserLetter;
import com.vk.user.mapper.ApUserLetterMapper;
import com.vk.user.service.ApUserLetterService;
import org.springframework.stereotype.Service;

/**
 * APP用户私信信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class ApUserLetterServiceImpl extends ServiceImpl<ApUserLetterMapper, ApUserLetter> implements ApUserLetterService {

}
