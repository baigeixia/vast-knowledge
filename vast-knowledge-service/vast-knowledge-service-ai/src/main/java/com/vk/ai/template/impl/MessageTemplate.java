package com.vk.ai.template.impl;

import com.mybatisflex.core.row.Db;
import com.vk.ai.domain.ModelList;
import com.vk.ai.domain.dto.GeneralMessageDto;
import com.vk.ai.enums.MessageStatus;
import com.vk.ai.mapper.ChatInfoMapper;
import com.vk.common.core.utils.threads.TaskVirtualExecutorUtil;
import com.vk.db.domain.aiMessage.AiMg;
import com.vk.db.repository.aiMessage.AiMgRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class MessageTemplate {

    private final AiMgRepository aiMgRepository;

    private final ChatInfoMapper chatInfoMapper;

    @Autowired
    public MessageTemplate(AiMgRepository aiMgRepository, ChatInfoMapper chatInfoMapper) {
        this.aiMgRepository = aiMgRepository;
        this.chatInfoMapper = chatInfoMapper;
    }

    public void saveMessage(String role, ModelList modelList, GeneralMessageDto dto,
                            Integer tokenNumber,
                            String searchContent, String reasoningContent,
                            String content, MessageStatus status) {
        TaskVirtualExecutorUtil.executeWith(() -> {
                    AiMg aiMg = buildMessage(role, modelList, dto, tokenNumber,searchContent, reasoningContent, content, status);
                    try {
                        aiMgRepository.save(aiMg);
                        Db.tx(()->{
                            chatInfoMapper.updateMessageIdAuto(aiMg.getInfoId());
                            if(null!=tokenNumber){
                                chatInfoMapper.updateTokenAuto(aiMg.getInfoId(),tokenNumber);
                            }
                            return true;
                        });
                        log.debug("保存消息成功: {}", aiMg);
                    } catch (Exception e) {
                        log.error("保存消息失败: {}", aiMg, e);
                        throw new RuntimeException("数据库保存失败", e);
                    }
                }
        );
    }

    private AiMg buildMessage(String role, ModelList modelList, GeneralMessageDto dto,
                              Integer token,
                              String searchContent, String reasoningContent,
                              String content, MessageStatus messageStatus) {

        Boolean searchEnabled = dto.getSearchEnabled();
        String searchStatus = null;

        if (searchEnabled != null && searchEnabled) {
            searchStatus = searchContent.isEmpty() ? MessageStatus.FAILED.toString() : MessageStatus.SUCCESS.toString();
        }

        LocalDateTime now = LocalDateTime.now();

        // 父级id
        Integer firstMessageId = getFirstMessageId(dto.getChatSessionId());
        if (null == firstMessageId || firstMessageId == 0) {
            // 没有 初始化
            firstMessageId = 1;
        }

        //本次消息id
        Integer messageId = firstMessageId + 1;
        //  叠加 回复用
        // dto.setParentMessageId(messageId);

        AiMg aiMg = new AiMg();
        aiMg.setInfoId(dto.getChatSessionId());
        // 本次 消息id 父级 +1
        aiMg.setMessageId(messageId);
        // 父级id
        aiMg.setParentId(firstMessageId);
        aiMg.setUsingTokens(token);
        aiMg.setModelId(modelList.getModelId());
        aiMg.setModelName(modelList.getModelName());
        aiMg.setRole(role);
        aiMg.setContent(content);
        aiMg.setThinkingEnabled(dto.getThinkingEnabled());
        aiMg.setThinkingContent(reasoningContent);
        aiMg.setSearchEnabled(searchEnabled);
        aiMg.setSearchStatus(searchStatus);
        aiMg.setSearchResults(searchContent);
        aiMg.setCreatingTime(now);
        aiMg.setUpdateTime(now);
        aiMg.setDel(false);
        aiMg.setStatus(messageStatus.toString());
        return aiMg;
    }

    public Integer getFirstMessageId(String infoId) {
        Optional<AiMg> aiMgOptional = aiMgRepository.findFirstByInfoIdAndDelFalseOrderByMessageIdDesc(infoId);
        return aiMgOptional.map(AiMg::getMessageId).orElse(null);
    }


    public List<AiMg> getOrderedMessages(String sessionId, int size) {
        Pageable pageable = PageRequest.of(0, size <= 0 ? 10 : size);
        return aiMgRepository.findByInfoIdAndDelFalseOrderByParentIdDesc(sessionId, pageable)
                .stream()
                .sorted(Comparator.comparing(AiMg::getParentId, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
    }

}
