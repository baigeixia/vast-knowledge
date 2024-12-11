package com.vk.comment.service;

import com.mybatisflex.core.service.IService;
import com.vk.comment.domain.ApComment;
import com.vk.comment.domain.dto.CommentSaveDto;
import com.vk.comment.domain.dto.UpCommentDto;
import com.vk.comment.domain.vo.CommentList;
import com.vk.comment.domain.vo.CommentListVo;
import com.vk.comment.domain.vo.NotificationListVo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * APP评论信息 服务层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApCommentService extends IService<ApComment> {

    CommentList saveComment(CommentSaveDto dto);

    void removeComment(Serializable id);

    void updateComment(UpCommentDto dto);

    CommentListVo getCommentList(Long notificationId,Serializable entryId, Integer type,Long page, Long size);

    List<NotificationListVo> getNotification(Integer page, Integer size);

    Long getArticleCommentById(Long id, LocalDateTime startTime, LocalDateTime endTime);
}
