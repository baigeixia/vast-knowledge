package com.vk.behaviour.mapper;

import com.mybatisflex.core.BaseMapper;
import com.vk.behaviour.domain.ApLikesBehavior;
import com.vk.behaviour.domain.entity.LikesBehaviorTimeCount;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * APP点赞行为 映射层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApLikesBehaviorMapper extends BaseMapper<ApLikesBehavior> {

    @Select("SELECT DATE(created_time) AS date from ap_follow_behavior GROUP BY DATE(created_time) ORDER BY DATE(created_time) DESC LIMIT  #{page}, #{size}")
    List<String> getTimeRange(@Param("page") Long page, @Param("size")Long size);

    @Select("SELECT article_id as articleId, comment_id as commentId ,COUNT(*) as total,DATE(created_time) as time  from  ap_likes_behavior a INNER JOIN (\n" +
            "SELECT DATE(created_time) AS date from ap_likes_behavior GROUP BY DATE(created_time) ORDER BY DATE(created_time) DESC LIMIT #{page} , #{size}\n" +
            ") b on  DATE(a.created_time) = b.date  WHERE repay_author_id=#{userId} and operation=0 GROUP BY article_id , comment_id,DATE(created_time) ORDER BY DATE(created_time) desc\n")
    List<LikesBehaviorTimeCount> getLikesBehaviorTimeCountList(@Param("userId") Long userId,@Param("page") Long page, @Param("size")Long size);

    List<ApLikesBehavior> selectUserLikes(@Param("userId")Long userId, @Param("ids")Set<Long> ids);

    List<ApLikesBehavior> selectUserCommentLikes(@Param("userId")Long userId,@Param("artId")Long artId, @Param("ids")Set<Long> ids);

    @Select("select article_id articleId ,repay_author_id repayAuthorId , created_time createdTime from ap_likes_behavior where author_id=#{userId} and type=1 and operation=0 ORDER BY  created_time DESC LIMIT #{page},#{size}")
    List<ApLikesBehavior> getLikesList(@Param("userId")Long userId,@Param("page") Long page, @Param("size")Long size);
}
