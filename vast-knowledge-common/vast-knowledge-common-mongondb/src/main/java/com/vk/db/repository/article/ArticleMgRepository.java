package com.vk.db.repository.article;


import com.vk.db.domain.article.ArticleMg;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * MongoTemplate的帮助类
 */
public interface ArticleMgRepository extends MongoRepository<ArticleMg,Long> {

    ArticleMg findByArticleId(Long id);









}
