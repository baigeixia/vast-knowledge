package com.vk.analyze.mapper;

import com.mybatisflex.core.BaseMapper;
import com.vk.analyze.domain.AdSensitive;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * 敏感词信息 映射层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface AdSensitiveMapper extends BaseMapper<AdSensitive> {


    List<AdSensitive> getlist(AdSensitive adSensitive);

    @Select("select sensitives from ad_sensitive where type=#{type}")
    Set<String> sensitiveSet(@Param("type") int i);
}
