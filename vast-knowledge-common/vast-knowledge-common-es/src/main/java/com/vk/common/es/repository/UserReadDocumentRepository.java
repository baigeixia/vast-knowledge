package com.vk.common.es.repository;

import com.vk.common.es.domain.UserInfoDocument;
import com.vk.common.es.domain.UserReadDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface UserReadDocumentRepository extends ElasticsearchRepository<UserReadDocument, Long> {
    List<UserReadDocument> findByEntryId(Long entryId);
}
