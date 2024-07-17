package com.vk.db.service.article;


import com.vk.db.domain.article.ArticleMg;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

/**
 * MongoTemplate的帮助类
 */
@Component
public interface ArticleMgService extends MongoRepository<ArticleMg,String> {


}
