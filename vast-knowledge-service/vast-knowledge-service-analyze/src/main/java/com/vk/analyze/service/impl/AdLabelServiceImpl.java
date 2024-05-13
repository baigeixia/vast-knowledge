package com.vk.analyze.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.analyze.domain.AdLabel;
import com.vk.analyze.domain.table.AdChannelTableDef;
import com.vk.analyze.domain.table.AdLabelTableDef;
import com.vk.analyze.mapper.AdLabelMapper;
import com.vk.analyze.service.AdLabelService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 标签信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class AdLabelServiceImpl extends ServiceImpl<AdLabelMapper, AdLabel> implements AdLabelService {

    @Override
    public List<AdLabel> getlist() {
        Page<AdLabel> paginate = this.mapper.paginate(1, 2, AdLabelTableDef.AD_LABEL.ID.eq(1));
        return paginate.getRecords();
    }
}
