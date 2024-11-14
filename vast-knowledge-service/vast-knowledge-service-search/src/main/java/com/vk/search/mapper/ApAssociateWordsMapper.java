package com.vk.search.mapper;

import com.mybatisflex.core.BaseMapper;
import com.vk.common.es.domain.HotWordsDocument;
import com.vk.search.domain.ApAssociateWords;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 联想词 映射层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApAssociateWordsMapper extends BaseMapper<ApAssociateWords> {


    @Select("select id,associate_words as associateWords,created_time as createdTime from ap_associate_words WHERE created_time <= #{now} LIMIT #{page}, #{size}  ")
    List<HotWordsDocument> selectByPage(@Param("page") long page, @Param("size")Long size, @Param("now")LocalDateTime now);
}
