package com.vk.article.listener;

import com.alibaba.fastjson2.JSON;
import com.vk.article.domain.ApArticle;
import com.vk.article.mapper.ApArticleMapper;
import com.vk.common.core.utils.threads.TaskVirtualExecutorUtil;
import com.vk.common.mq.common.MqConstants;
import com.vk.common.mq.domain.ArticleVisitStreamMess;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@Slf4j(topic = "ArticleStreamListener")
public class ArticleStreamListener {

    @Autowired
    private ApArticleMapper apArticleMapper;

    @KafkaListener(topics = MqConstants.TopicCS.HOT_ARTICLE_INCR_HANDLE_TOPIC, groupId = MqConstants.NOTIFY_GROUP)
    public void upOrDown(ConsumerRecord<String, String> record) {
        int p = record.partition();
        long o = record.offset();
        String value = record.value();
        ArticleVisitStreamMess mess = JSON.parseObject(value, ArticleVisitStreamMess.class);
        if (!ObjectUtils.isEmpty(mess)) {
            TaskVirtualExecutorUtil.executeWith(() -> {
                Long id = mess.getArticleId();
                ApArticle article = apArticleMapper.selectOneById(id);
                if (!ObjectUtils.isEmpty(article)) {
                    ApArticle insertArticle = new ApArticle();
                    insertArticle.setId(id);
                    insertArticle.setLikes(article.getLikes() + mess.getLike());
                    insertArticle.setCollection(article.getCollection() + mess.getCollect());
                    insertArticle.setComment(article.getComment() + mess.getComment());
                    insertArticle.setViews(article.getViews() + mess.getView());
                    apArticleMapper.update(insertArticle);
                }else {
                    log.error("文章流保存错误,文章信息为空");
                }
            });
        }
    }


}
