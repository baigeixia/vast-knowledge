package com.vk.comment.service;

import com.mybatisflex.core.service.IService;
import com.vk.comment.domain.ApComment;
import com.vk.comment.domain.dto.CommentSaveDto;
import com.vk.comment.domain.dto.UpCommentDto;
import com.vk.comment.domain.vo.CommentList;
import com.vk.comment.domain.vo.CommentListVo;

import java.io.Serializable;

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

    CommentListVo getCommentList(Serializable entryId, Integer type,Long page, Long size);
}
