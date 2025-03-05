package com.vk.db.repository.article;


import com.vk.db.domain.article.ArticleMg;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * MongoTemplate的帮助类
 */
public interface ArticleMgRepository extends MongoRepository<ArticleMg,Long> {

    ArticleMg findByArticleIdAndAuthorId(Long id,Long userid);
    ArticleMg findByArticleIda(Long id);









}
