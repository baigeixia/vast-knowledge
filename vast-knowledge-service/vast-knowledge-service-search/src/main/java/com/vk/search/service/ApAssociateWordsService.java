package com.vk.search.service;

import com.mybatisflex.core.service.IService;
import com.vk.search.domain.ApAssociateWords;
import com.vk.search.domain.vo.AssociateListVo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 联想词 服务层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApAssociateWordsService extends IService<ApAssociateWords> {

    List<AssociateListVo> getESList(String text);


    void importAll(long page, Long size, CountDownLatch countDownLatch, LocalDateTime now);
}
