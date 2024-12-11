package com.vk.comment.mapper;

import com.mybatisflex.core.BaseMapper;
import com.vk.comment.domain.ApComment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

/**
 * APP评论信息 映射层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApCommentMapper extends BaseMapper<ApComment> {

    @Update("update ap_comment set status = #{status} where comment_id = #{commentId}")
    void upAllComment(@Param("commentId") Long commentId, @Param("status") int status);

    @Select("SELECT a.id id from  ap_comment a LEFT JOIN  ap_comment_repay b on a.id=b.comment_id WHERE a.id = #{id} or b.id=#{id} LIMIT 1")
    Long selectGetNotificationCommentId(@Param("id") Long notificationId);

    Long getArticleCommentById(@Param("articleId")Long articleId, @Param("startTime")LocalDateTime startTime, @Param("endTime")LocalDateTime endTime);
}
