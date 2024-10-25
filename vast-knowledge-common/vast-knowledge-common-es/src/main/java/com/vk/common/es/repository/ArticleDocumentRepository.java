package com.vk.common.es.repository;


import com.vk.common.es.domain.ArticleInfoDocument;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.HighlightParameters;
import org.springframework.data.elasticsearch.annotations.SourceFilters;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

public interface ArticleDocumentRepository extends ElasticsearchRepository<ArticleInfoDocument, Long> {

    @Highlight(
            fields = {@HighlightField(name = "title")},
            parameters = @HighlightParameters(preTags = {"<span style='color:red'>"}, postTags = {"</span>"})
    )
    @SourceFilters(includes = {"title", "id"})
    SearchHits<ArticleInfoDocument> findByTitle(@Param("title") String title);

}
