package com.vk.search.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.search.domain.ApHotWords;
import com.vk.search.mapper.ApHotWordsMapper;
import com.vk.search.service.ApHotWordsService;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.vk.search.domain.table.ApHotWordsTableDef.AP_HOT_WORDS;

/**
 * 搜索热词 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class ApHotWordsServiceImpl extends ServiceImpl<ApHotWordsMapper, ApHotWords> implements ApHotWordsService {

    @Override
    public List<ApHotWords> getTrending() {
        List<ApHotWords> apHotWords = mapper.selectListByQuery(QueryWrapper.create().select(AP_HOT_WORDS.HOT_WORDS, AP_HOT_WORDS.TYPE).orderBy(AP_HOT_WORDS.CREATED_TIME, false).limit(10));
        return apHotWords;
    }
}
