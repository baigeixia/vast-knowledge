package com.vk.wemedia.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.vk.wemedia.domain.WmNews;

/**
 * 自媒体图文内容信息 服务层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface WmNewsService extends IService<WmNews> {

    Page<WmNews> getlist(WmNews news);
}
