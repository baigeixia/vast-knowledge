package com.vk.common.es.repository;


import com.vk.common.es.domain.AssociateWordsDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AssociateWordsDocumentRepository extends ElasticsearchRepository<AssociateWordsDocument, Long> {


}
