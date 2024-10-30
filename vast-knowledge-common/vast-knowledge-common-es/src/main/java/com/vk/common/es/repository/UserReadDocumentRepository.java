package com.vk.common.es.repository;

import com.vk.common.es.domain.UserInfoDocument;
import com.vk.common.es.domain.UserReadDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserReadDocumentRepository extends ElasticsearchRepository<UserReadDocument, Long> {
}
