package com.vk.wemedia.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.common.core.utils.ServletUtils;
import com.vk.common.core.utils.StringUtils;
import com.vk.wemedia.domain.WmNews;
import com.vk.wemedia.domain.table.WmNewsTableDef;
import com.vk.wemedia.mapper.WmNewsMapper;
import com.vk.wemedia.service.WmNewsService;
import org.springframework.stereotype.Service;

import static com.vk.wemedia.domain.table.WmNewsTableDef.WM_NEWS;

/**
 * 自媒体图文内容信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {

    @Override
    public Page<WmNews> getlist(WmNews news) {
        Integer status = news.getStatus();
        String title = news.getTitle();

        Integer pageNum = ServletUtils.getPageNum();
        Integer pageSize = ServletUtils.getPageSize();

        Page<WmNews> paginate = this.mapper.paginate(pageNum, pageSize, query()
                .where(WM_NEWS.STATUS.eq(status,null!=status ))
                .and(WM_NEWS.TITLE.like(title, StringUtils.isNotEmpty(title))));
        return paginate;
    }
}
