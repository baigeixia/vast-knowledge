package com.vk.comment.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.vk.comment.domain.ApCommentRepay;
import com.vk.comment.domain.dto.CommentReSaveDto;
import com.vk.comment.domain.vo.CommentList;
import com.vk.comment.domain.vo.CommentListRe;

import java.io.Serializable;
import java.util.List;

/**
 * APP评论回复信息 服务层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApCommentRepayService extends IService<ApCommentRepay> {

    void saveCommentRe(CommentReSaveDto dto);

    void removeCommentRe(Serializable id);

    List<CommentListRe> getCommentReList(Serializable commentId, Long page, Long size);
}
