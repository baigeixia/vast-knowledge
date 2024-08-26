package com.vk.comment.repository;

import com.vk.comment.document.ApCommentDocument;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface CommentDocumentRepository extends ElasticsearchRepository<ApCommentDocument, Long> {

    @Highlight(fields = {
            @HighlightField(name = "content")
    })
    SearchHits<ApCommentDocument> findByEntryIdAndId(@Param("EntryId") Long entryId,@Param("Id") Long id,@Param("text") String text);

    @Highlight(fields = {
            @HighlightField(name = "content")
    })
    SearchHits<ApCommentDocument> findByContent(@Param("text") String text);

    List<ApCommentDocument> findAllByEntryIdInOrderByCreatedTimeDesc(@Param("entryIds") Integer[] ids, Pageable pageable);
}
