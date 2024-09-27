package com.vk.behaviour.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.article.domain.HomeArticleListVo;
import com.vk.article.feign.RemoteClientArticleQueryService;
import com.vk.behaviour.domain.ApCollectBehavior;
import com.vk.behaviour.mapper.ApCollectBehaviorMapper;
import com.vk.behaviour.service.ApCollectBehaviorService;
import com.vk.common.core.domain.R;
import com.vk.common.core.domain.ValidationUtils;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * APP收藏行为 服务层实现。
 *
 * @author 张三
 * @since 2024-09-03
 */
@Service
public class ApCollectBehaviorServiceImpl extends ServiceImpl<ApCollectBehaviorMapper, ApCollectBehavior> implements ApCollectBehaviorService {

    @Autowired
    private RemoteClientArticleQueryService remoteClientArticleQueryService;
    @Override
    public List<HomeArticleListVo> userCollectList(Long page, Long size, Long userId) {
        if (StringUtils.isLongEmpty(userId)) {
            userId = RequestContextUtil.getUserId();
        }
        List<HomeArticleListVo> result= new ArrayList<>();
        page=(page-1)*size;
        Set<Long> articleIds= mapper.selectUserCollectIdS(userId,page,size);
        R<Map<Long, HomeArticleListVo>> idList = remoteClientArticleQueryService.getArticleIdList(articleIds);
        if (ValidationUtils.validateRSuccess(idList)) {
            return  idList.getData().values().stream().toList();
        }
        return result;
    }
}
