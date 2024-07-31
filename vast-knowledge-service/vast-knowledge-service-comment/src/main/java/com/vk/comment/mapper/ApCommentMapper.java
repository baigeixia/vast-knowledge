package com.vk.comment.mapper;

import com.mybatisflex.core.BaseMapper;
import com.vk.comment.domain.ApComment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * APP评论信息 映射层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApCommentMapper extends BaseMapper<ApComment> {

    @Update("update ap_comment set status = #{status} where comment_id = #{commentId}")
    void upAllComment(@Param("commentId") Long commentId, @Param("status") int status);
}
