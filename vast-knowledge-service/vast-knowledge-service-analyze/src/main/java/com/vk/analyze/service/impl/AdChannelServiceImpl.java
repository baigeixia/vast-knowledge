package com.vk.analyze.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.query.QueryCondition;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.analyze.domain.AdChannel;
import com.vk.analyze.domain.table.AdChannelTableDef;
import com.vk.analyze.mapper.AdChannelMapper;
import com.vk.analyze.service.AdChannelService;
import com.vk.common.core.web.page.PageDomain;
import com.vk.common.core.web.page.TableSupport;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.vk.analyze.domain.table.AdChannelTableDef.AD_CHANNEL;

/**
 * 频道信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class AdChannelServiceImpl extends ServiceImpl<AdChannelMapper, AdChannel> implements AdChannelService {

    @Override
    public List<AdChannel> getlist(AdChannel adChannel) {

//        QueryCondition eq = AD_CHANNEL.STATUS.eq(adChannel.getStatus(), null!=adChannel.getStatus());
        QueryWrapper eq = query().where(AD_CHANNEL.STATUS.eq(adChannel.getStatus()));
        Page<AdChannel> paginate = this.mapper.paginate(1, 10, eq);
        return paginate.getRecords();
    }
}
