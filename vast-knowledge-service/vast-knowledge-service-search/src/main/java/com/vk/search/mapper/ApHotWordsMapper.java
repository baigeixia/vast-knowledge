package com.vk.search.mapper;

import com.mybatisflex.core.BaseMapper;
import com.vk.search.domain.ApHotWords;
import com.vk.search.domain.HotWordsTop;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 搜索热词 映射层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApHotWordsMapper extends BaseMapper<ApHotWords> {

    @Select("SELECT keyword,COUNT(*) AS searchCount FROM ap_user_search WHERE STATUS = 1 GROUP BY keyword ORDER BY search_count DESC LIMIT 10;")
    List<HotWordsTop> selectTopHotList();
}
