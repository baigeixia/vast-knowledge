package com.vk.common.es.repository;


import com.vk.common.es.domain.ArticleInfoDocument;
import com.vk.common.es.domain.HotWordsDocument;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.HighlightParameters;
import org.springframework.data.elasticsearch.annotations.SourceFilters;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

public interface HotWordsDocumentRepository extends ElasticsearchRepository<HotWordsDocument, Long> {


}
