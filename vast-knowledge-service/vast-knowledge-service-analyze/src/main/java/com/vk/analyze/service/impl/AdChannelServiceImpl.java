package com.vk.analyze.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.analyze.domain.AdChannel;
import com.vk.analyze.mapper.AdChannelMapper;
import com.vk.analyze.service.AdChannelService;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.ServletUtils;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.redis.constants.BusinessConstants;
import com.vk.common.redis.service.RedisService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;

import static com.vk.analyze.domain.table.AdChannelTableDef.AD_CHANNEL;
import static com.vk.common.core.constant.DatabaseConstants.DB_ROW_STATUS_YES;

/**
 * 频道信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class AdChannelServiceImpl extends ServiceImpl<AdChannelMapper, AdChannel> implements AdChannelService {

    @Autowired
    private RedisService redisService;


    /**
     * 项目启动时，初始化字典到缓存
     */
    @PostConstruct
    public void init()
    {
        loadingChannel();
    }

    @Override
    public void loadingChannel() {
        mapper.selectListByQuery(QueryWrapper.create().where(AD_CHANNEL.STATUS.eq(DB_ROW_STATUS_YES)))
                .forEach(i->
                        redisService.setCacheObject(BusinessConstants.loadingChannel(i.getId()), i)
                );
    }

    @Override
    public void removeByIdone(Serializable id) {
        AdChannel adChannel = mapper.selectOneById(id);
        if (ObjectUtils.isEmpty(adChannel)){
            throw new LeadNewsException("频道不存在 可能已被移除");
        }

        mapper.deleteById(id);
    }


    @Override
    public  Page<AdChannel>  getlist(AdChannel adChannel) {
        Integer status = adChannel.getStatus();
        String name = adChannel.getName();

        Integer pageNum = ServletUtils.getPageNum();
        Integer pageSize = ServletUtils.getPageSize();

        //        QueryCondition eq = AD_CHANNEL.STATUS.eq(adChannel.getStatus(), null!=adChannel.getStatus());

        Page<AdChannel> paginate = this.mapper.paginate(pageNum, pageSize, query()
                .where(AD_CHANNEL.STATUS.eq(status,null!=status ))
                .and(AD_CHANNEL.NAME.like(name, StringUtils.isNotEmpty(name))));

        return paginate;

    }
}
