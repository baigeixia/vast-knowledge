package com.vk.behaviour.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.article.feign.RemoteClientArticleQueryService;
import com.vk.behaviour.domain.ApLikesBehavior;
import com.vk.behaviour.domain.entity.LikesBehaviorTimeCount;
import com.vk.behaviour.domain.vo.notification.like.AttachInfo;
import com.vk.behaviour.domain.vo.notification.like.LikeActors;
import com.vk.behaviour.domain.vo.notification.like.LikeNotificationInfo;
import com.vk.behaviour.domain.vo.notification.like.LikeNotificationListVo;
import com.vk.behaviour.mapper.ApLikesBehaviorMapper;
import com.vk.behaviour.service.ApLikesBehaviorService;
import com.vk.common.core.domain.R;
import com.vk.common.core.domain.ValidationUtils;
import com.vk.common.core.exception.CustomSimpleThrowUtils;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.vk.behaviour.domain.table.ApLikesBehaviorTableDef.AP_LIKES_BEHAVIOR;

/**
 * APP点赞行为 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
@Slf4j
public class ApLikesBehaviorServiceImpl extends ServiceImpl<ApLikesBehaviorMapper, ApLikesBehavior> implements ApLikesBehaviorService {

    @Autowired
    private RemoteClientArticleQueryService remoteClientArticleQueryService;

    @Override
    public List<LikeNotificationListVo> likeList(Long page, Long size) {
        List<LikeNotificationListVo> vos = new ArrayList<>();
        Long userId = RequestContextUtil.getUserId();
        // List<String> dataTime= mapper.getTimeRange((page-1)*size,size);
        List<LikesBehaviorTimeCount> rawData = mapper.getLikesBehaviorTimeCountList(userId, (page - 1) * size, size);
        if (!ObjectUtils.isEmpty(rawData)) {
            Set<Long> articleIdSets = rawData.stream().map(LikesBehaviorTimeCount::getArticleId).collect(Collectors.toSet());
            R<Map<Long, String>> title = remoteClientArticleQueryService.getArticleTitle(articleIdSets);

            ValidationUtils.validateR(title, "文章服务未知异常");

            vos = rawData.stream()
                    .collect(Collectors.groupingBy(
                            LikesBehaviorTimeCount::getTime,
                            Collectors.mapping(
                                    data -> {
                                        LikeNotificationInfo info = new LikeNotificationInfo();
                                        info.setVerb(StringUtils.isLongEmpty(data.getCommentId()) ? "喜欢了您的文章" : "喜欢了您的评论");
                                        info.setMergeCount(data.getTotal());
                                        List<LikeActors> actors = mapper.selectListByQueryAs(
                                                QueryWrapper.create().select(
                                                        AP_LIKES_BEHAVIOR.AUTHOR_ID.as(LikeActors::getId),
                                                        AP_LIKES_BEHAVIOR.AUTHOR_NAME.as(LikeActors::getUsername),
                                                        AP_LIKES_BEHAVIOR.CREATED_TIME.as(LikeActors::getReplyContentTime)
                                                ).where(
                                                        AP_LIKES_BEHAVIOR.ARTICLE_ID.eq(data.getArticleId())
                                                                .and(AP_LIKES_BEHAVIOR.COMMENT_ID.eq(data.getCommentId(), !StringUtils.isLongEmpty(data.getCommentId())))
                                                ).limit(3), LikeActors.class
                                        );
                                        info.setActors(actors);
                                        String datetime = actors.getLast().getReplyContentTime().toLocalTime().toString();
                                        info.setCommentEndTime(datetime);
                                        info.setAttachInfo(new AttachInfo(
                                                data.getArticleId(),
                                                data.getCommentId(),
                                                title.getData().get(data.getArticleId())
                                        ));
                                        return info;
                                    },
                                    Collectors.toList()
                            )
                    ))
                    .entrySet().stream()
                    .map(entry -> {
                        LikeNotificationListVo listVo = new LikeNotificationListVo();
                        listVo.setStatisticsTime(entry.getKey());
                        listVo.setNotificationInfoList(entry.getValue());
                        return listVo;
                    })
                    .toList();

        }

        return vos;
    }





    @Override
    public Map<Long, Integer> articleLike(Set<Long> ids) {
        CustomSimpleThrowUtils.ObjectIsEmpty(ids, "参数错误");
        Long userId = RequestContextUtil.getUserId();

        List<ApLikesBehavior> userLikes = mapper.selectUserLikes(userId, ids);
        return userLikes.stream().collect(Collectors.toMap(ApLikesBehavior::getArticleId, ApLikesBehavior::getOperation, (existingValue, newValue) -> newValue));

    }

    @Override
    public Map<Long, Integer> commentLike(Long artId,Set<Long> ids) {
        CustomSimpleThrowUtils.ObjectIsEmpty(ids, "参数错误");
        CustomSimpleThrowUtils.LongIsEmpty(artId, "id错误");
        Long userId = RequestContextUtil.getUserId();
        List<ApLikesBehavior> userLikes = mapper.selectUserCommentLikes(userId, artId,ids);

        return userLikes.stream().collect(Collectors.toMap(ApLikesBehavior::getCommentId, ApLikesBehavior::getOperation, (existingValue, newValue) -> newValue));

    }
}
