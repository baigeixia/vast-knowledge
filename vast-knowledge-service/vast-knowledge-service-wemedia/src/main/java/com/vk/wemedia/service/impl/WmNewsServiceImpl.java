package com.vk.wemedia.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.wemedia.domain.WmNews;
import com.vk.wemedia.mapper.WmNewsMapper;
import com.vk.wemedia.service.WmNewsService;
import org.springframework.stereotype.Service;

/**
 * 自媒体图文内容信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {

}
