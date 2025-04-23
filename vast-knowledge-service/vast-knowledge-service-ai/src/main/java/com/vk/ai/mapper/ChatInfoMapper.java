package com.vk.ai.mapper;

import com.mybatisflex.core.BaseMapper;
import com.vk.ai.domain.ChatInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 消息详情 映射层。
 *
 * @author 张三
 * @since 2025-04-15
 */
public interface ChatInfoMapper extends BaseMapper<ChatInfo> {

    @Select("UPDATE chat_info SET current_message_id = current_message_id + 1 WHERE id = #{id};")
    void updateMessageIdAuto(@Param("id") String id);

    @Update("UPDATE chat_info SET title = #{title} WHERE id = #{id};")
    void updateMessageTitle(@Param("id")String sessionId, @Param("title")String title);

    @Update("UPDATE chat_info SET used_token = used_token + #{usedTokens} WHERE id = #{id};")
    void updateTokenAuto(@Param("id")String infoId, @Param("usedTokens")Integer tokenNumber);
}
