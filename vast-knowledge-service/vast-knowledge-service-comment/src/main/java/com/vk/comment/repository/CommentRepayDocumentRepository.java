package com.vk.comment.repository;

import com.vk.comment.document.ApCommentDocument;
import com.vk.comment.document.ApCommentRepayDocument;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CommentRepayDocumentRepository extends ElasticsearchRepository<ApCommentRepayDocument, Long> {


}
