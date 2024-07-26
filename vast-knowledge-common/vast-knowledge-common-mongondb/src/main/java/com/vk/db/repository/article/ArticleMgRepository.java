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


    @Query("update articleMg a set a.content = ?2 where a.articleId = ?1")
    void updateByArticleId(Long articleId, String content);
}
