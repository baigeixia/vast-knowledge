package com.vk.behaviour.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.article.domain.HomeArticleListVo;
import com.vk.article.feign.RemoteClientArticleQueryService;
import com.vk.behaviour.domain.ApBehaviorEntry;
import com.vk.behaviour.domain.ApCollectBehavior;
import com.vk.behaviour.domain.ApLikesBehavior;
import com.vk.behaviour.domain.vo.BehaviorListVo;
import com.vk.behaviour.mapper.ApBehaviorEntryMapper;
import com.vk.behaviour.mapper.ApCollectBehaviorMapper;
import com.vk.behaviour.mapper.ApLikesBehaviorMapper;
import com.vk.behaviour.service.ApBehaviorEntryService;
import com.vk.common.core.domain.R;
import com.vk.common.core.domain.ValidationUtils;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.utils.StringUtils;
import com.vk.user.feign.RemoteClientUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * APP行为实体,一个行为实体可能是用户或者设备，或者其它 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class ApBehaviorEntryServiceImpl extends ServiceImpl<ApBehaviorEntryMapper, ApBehaviorEntry> implements ApBehaviorEntryService {

    @Autowired
    private ApLikesBehaviorMapper apLikesBehaviorMapper;

    @Autowired
    private ApCollectBehaviorMapper apCollectBehaviorMapper;

    @Autowired
    private RemoteClientArticleQueryService remoteClientArticleQueryService;

    @Autowired
    private RemoteClientUserService remoteClientUserService;

    @Override
    public List<BehaviorListVo> getList(Long userId, Long page, Long size) {
        if (StringUtils.isLongEmpty(userId)) {
            userId = RequestContextUtil.getUserId();
        }
        List<BehaviorListVo> vos = new ArrayList<>();

        page = (page - 1) * size;

        List<ApLikesBehavior> likesList = apLikesBehaviorMapper.getLikesList(userId, page, size);
        List<ApCollectBehavior> collectBehaviors = apCollectBehaviorMapper.getcollectBehaviorsList(userId, page, size);

        // 文章id
        Set<Long> articleIds = new HashSet<>();
        // Set<Long> likeIds = new HashSet<>();
        Map<Long, LocalDateTime> likeInfo = new HashMap<>();
        Map<Long, LocalDateTime> collectInfo = new HashMap<>();
        // Set<Long> collectIds = new HashSet<>();
        // 回复用户id
        // Set<Long> repayAuthorIdS = new HashSet<>();

        for (ApLikesBehavior like : likesList) {
            // likeIds.add(like.getArticleId());
            likeInfo.put(like.getArticleId(), like.getCreatedTime());
            // repayAuthorIdS.add(like.getRepayAuthorId());
        }

        for (ApCollectBehavior collect : collectBehaviors) {
            // collectIds.add(collect.getArticleId());
            collectInfo.put(collect.getArticleId(), collect.getCreatedTime());
            // repayAuthorIdS.add(collect.getRepayAuthorId());
        }
        Set<Long> likeIdset = likeInfo.keySet();
        Set<Long> collectIdset = collectInfo.keySet();
        articleIds.addAll(likeIdset);
        articleIds.addAll(collectIdset);

        R<Map<Long, HomeArticleListVo>> idList = remoteClientArticleQueryService.getBehaviorArticleIdList(userId, page, articleIds);
        Map<Long, HomeArticleListVo> idListData = idList.getData();
        if (ValidationUtils.validateRSuccess(idList) && null != idListData) {

            return idListData.values().stream().map(i -> {
                        BehaviorListVo vo = new BehaviorListVo();
                        Long id = i.getId();
                        if (likeIdset.contains(id)) {
                            vo.setActionText("赞同了文章");
                            vo.setCreatedTime(likeInfo.get(id));
                        } else if (collectIdset.contains(id)) {
                            vo.setActionText("收藏了文章");
                            vo.setCreatedTime(collectInfo.get(id));
                        } else {
                            vo.setActionText("发布了文章");
                            vo.setCreatedTime(i.getCreatedTime());
                        }

                        vo.setTarget(i);
                        return vo;
                    })
                    .sorted((vo1, vo2) -> vo2.getCreatedTime().compareTo(vo1.getCreatedTime())) // 根据 createdTime 排序
                    .toList();
        }

        return vos;
    }
}
