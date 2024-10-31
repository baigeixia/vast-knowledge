package com.vk.user.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.utils.StringUtils;
import com.vk.user.domain.ApUserFan;
import com.vk.user.domain.table.ApUserFanTableDef;
import com.vk.user.domain.vo.FanListVo;
import com.vk.user.mapper.ApUserFanMapper;
import com.vk.user.service.ApUserFanService;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.vk.user.domain.table.ApUserFanTableDef.AP_USER_FAN;

/**
 * APP用户粉丝信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class ApUserFanServiceImpl extends ServiceImpl<ApUserFanMapper, ApUserFan> implements ApUserFanService {

    @Override
    public Page<FanListVo> getList(Long page, Long size, Long userId) {

        if (StringUtils.isLongEmpty(userId)) {
            userId = RequestContextUtil.getUserId();
        }

        List<FanListVo> fanListVos = mapper.getList(userId, (page-1)*size,size,userId);
        return new Page<>(fanListVos,page,size,0L);
    }
}
