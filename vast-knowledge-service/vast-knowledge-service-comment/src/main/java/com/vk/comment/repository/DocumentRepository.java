package com.vk.comment.repository;

import com.vk.comment.document.ApCommentDocument;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.awt.print.Book;

public interface DocumentRepository extends ElasticsearchRepository<ApCommentDocument, Long> {

    @Highlight(fields = {
            @HighlightField(name = "content")
    })
    SearchHits<ApCommentDocument> findByEntryIdAndId(@Param("EntryId") Long entryId,@Param("Id") Long id,@Param("text") String text);

    @Highlight(fields = {
            @HighlightField(name = "content")
    })
    SearchHits<ApCommentDocument> findByContent(@Param("text") String text);
}
