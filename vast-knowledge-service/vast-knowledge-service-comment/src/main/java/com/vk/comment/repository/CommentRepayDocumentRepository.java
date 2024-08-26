package com.vk.comment.repository;

import com.vk.comment.document.ApCommentRepayDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CommentRepayDocumentRepository extends ElasticsearchRepository<ApCommentRepayDocument, Long> {

}
