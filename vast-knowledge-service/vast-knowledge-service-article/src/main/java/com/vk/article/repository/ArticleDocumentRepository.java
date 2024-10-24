package com.vk.article.repository;


import com.vk.article.domain.ArticleInfoDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ArticleDocumentRepository extends ElasticsearchRepository<ArticleInfoDocument, Long> {

}
