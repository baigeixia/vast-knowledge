package com.vk.common.es.repository;

import com.vk.common.es.domain.UserInfoDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserInfoDocumentRepository extends ElasticsearchRepository<UserInfoDocument, Long> {
}
