package com.vk.analyze.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.analyze.domain.AdLabel;
import com.vk.analyze.domain.table.AdLabelTableDef;
import com.vk.analyze.mapper.AdLabelMapper;
import com.vk.analyze.service.AdLabelService;
import com.vk.common.redis.service.RedisService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.vk.common.core.constant.CacheConstants.AD_CHANNEL_KEY;
import static com.vk.common.core.constant.CacheConstants.AD_CHANNEL_LABEL_KEY;
import static com.vk.common.redis.constants.BusinessConstants.loadingLabel;

/**
 * 标签信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class AdLabelServiceImpl extends ServiceImpl<AdLabelMapper, AdLabel> implements AdLabelService {


    @Autowired
    private RedisService redisService;


    /**
     * 项目启动时，初始化字典到缓存
     */
    @PostConstruct
    public void init() {
        loadingChannelLabel();
    }

    @Override
    public void loadingChannelLabel() {
        mapper.selectAll().forEach(i -> redisService.setCacheObject(loadingLabel(i.getId()), i));

    }



    @Override
    public List<AdLabel> getlist() {
        Page<AdLabel> paginate = this.mapper.paginate(1, 2, AdLabelTableDef.AD_LABEL.ID.eq(1));
        return paginate.getRecords();
    }
}
