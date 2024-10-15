package com.vk.behaviour.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.article.domain.HomeArticleListVo;
import com.vk.article.feign.RemoteClientArticleQueryService;
import com.vk.behaviour.domain.ApReadBehavior;
import com.vk.behaviour.domain.entity.LikesBehaviorTimeCount;
import com.vk.behaviour.domain.vo.UserFootMarkListVo;
import com.vk.behaviour.domain.vo.notification.like.AttachInfo;
import com.vk.behaviour.domain.vo.notification.like.LikeActors;
import com.vk.behaviour.domain.vo.notification.like.LikeNotificationInfo;
import com.vk.behaviour.domain.vo.notification.like.LikeNotificationListVo;
import com.vk.behaviour.mapper.ApReadBehaviorMapper;
import com.vk.behaviour.service.ApReadBehaviorService;
import com.vk.common.core.domain.R;
import com.vk.common.core.domain.ValidationUtils;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.utils.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.vk.behaviour.domain.table.ApLikesBehaviorTableDef.AP_LIKES_BEHAVIOR;
import static com.vk.behaviour.domain.table.ApReadBehaviorTableDef.AP_READ_BEHAVIOR;

/**
 * APP阅读行为 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class ApReadBehaviorServiceImpl extends ServiceImpl<ApReadBehaviorMapper, ApReadBehavior> implements ApReadBehaviorService {

    @Autowired
    private RemoteClientArticleQueryService remoteClientArticleQueryService;

    @Override
    public List<UserFootMarkListVo> getUserFootMark(Long page, Long size) {
        List<UserFootMarkListVo> vos = new ArrayList<>();
        Long userId = RequestContextUtil.getUserId();
        page = (page - 1) * size;
        List<ApReadBehavior> readBehaviors = mapper.selectListUserRead(userId, page, size);
        Set<Long> ids = readBehaviors.stream().map(ApReadBehavior::getArticleId).collect(Collectors.toSet());
        if (!ObjectUtils.isEmpty(ids)) {
            R<Map<Long, HomeArticleListVo>> idList = remoteClientArticleQueryService.getArticleIdList(ids);
            ValidationUtils.validateR(idList, "文章查询失败");
            Map<Long, HomeArticleListVo> idListData = idList.getData();
            vos = readBehaviors.stream()
                    .collect(Collectors.groupingBy( ApReadBehavior::getUpdatedTime,
                            Collectors.mapping( data->
                                        idListData.get(data.getArticleId()),
                                    Collectors.toList()
                            )
                    ))
                    .entrySet().stream()
                    .map(entry -> {
                        UserFootMarkListVo  listVo = new UserFootMarkListVo();
                        listVo.setFootMark(entry.getValue());
                        listVo.setDateTime(entry.getKey());
                        return listVo;
                    })
                    .sorted(Comparator.comparing(UserFootMarkListVo::getDateTime).reversed())  // Sort by statisticsTime in descending order
                    .toList();
        }

        return vos;
    }

    @Override
    public ApReadBehavior getArticleInfo(Long id) {
        Long userId = RequestContextUtil.getUserId();
        QueryWrapper wrapper = QueryWrapper.create().select();
        wrapper.where(AP_READ_BEHAVIOR.ARTICLE_ID.eq(id).and(AP_READ_BEHAVIOR.ENTRY_ID.eq(userId)));
        return mapper.selectOneByQuery(wrapper);
    }
}
