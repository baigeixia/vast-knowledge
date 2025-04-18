package com.vk.db.repository.aiMessage;


import com.vk.db.domain.aiMessage.AiMg;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * MongoTemplate的帮助类
 */
public interface AiMgRepository extends MongoRepository<AiMg,String> {

    List<AiMg> findByInfoIdAndDelFalseOrderByParentIdDesc(String infoId, Pageable pageable);

    Slice<AiMg> findByInfoIdAndDelFalseOrderByUpdateTimeAsc(String infoId, Pageable pageable);
    Slice<AiMg> findByInfoIdAndDelFalseOrderByMessageIdDesc(String infoId, Pageable pageable);
    Optional<AiMg> findFirstByInfoIdAndDelFalseOrderByMessageIdDesc(String infoId);

}
