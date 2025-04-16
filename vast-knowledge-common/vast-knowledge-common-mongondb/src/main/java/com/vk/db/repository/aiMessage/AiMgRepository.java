package com.vk.db.repository.aiMessage;


import com.vk.db.domain.aiMessage.AiMg;
import com.vk.db.domain.article.ArticleMg;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * MongoTemplate的帮助类
 */
public interface AiMgRepository extends MongoRepository<AiMg,String> {

    List<AiMg> findByInfoIdAndDelFalseOrderByParentIdDesc(String infoId, Pageable pageable);

}
