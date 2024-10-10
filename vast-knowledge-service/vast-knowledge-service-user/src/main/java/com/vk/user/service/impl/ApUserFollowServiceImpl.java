package com.vk.user.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.utils.StringUtils;
import com.vk.user.domain.ApUserFollow;
import com.vk.user.domain.vo.FanListVo;
import com.vk.user.domain.vo.FollowListVo;
import com.vk.user.mapper.ApUserFollowMapper;
import com.vk.user.service.ApUserFollowService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * APP用户关注信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class ApUserFollowServiceImpl extends ServiceImpl<ApUserFollowMapper, ApUserFollow> implements ApUserFollowService {

    @Override
    public Page<FollowListVo> getList(Long page, Long size, Long userId) {
        Long localID = RequestContextUtil.getUserId();

        if (StringUtils.isLongEmpty(userId)) {
            userId = localID;
        }

        List<FollowListVo> followListVos = mapper.getList(userId, (page-1)*size,size,localID);
        return new Page<>(followListVos,page,size,0L);
    }
}
