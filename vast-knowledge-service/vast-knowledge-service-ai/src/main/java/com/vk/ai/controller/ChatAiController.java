package com.vk.ai.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mybatisflex.core.query.QueryWrapper;
import com.vk.ai.domain.ChatInfo;
import com.vk.ai.domain.ModelList;
import com.vk.ai.domain.dto.CreateMessageDto;
import com.vk.ai.domain.dto.GeneralMessageDto;
import com.vk.ai.domain.dto.createChatDto;
import com.vk.ai.domain.table.ChatInfoTableDef;
import com.vk.ai.domain.table.ModelListTableDef;
import com.vk.ai.enums.AiType;
import com.vk.ai.mapper.ChatInfoMapper;
import com.vk.ai.mapper.ChatMessageMapper;
import com.vk.ai.service.ModelListService;
import com.vk.ai.strategy.AiTemplateStrategyContext;
import com.vk.ai.template.AbstractAiTemplate;
import com.vk.ai.template.AiTemplate;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.web.domain.AjaxResult;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static com.vk.ai.domain.table.ChatInfoTableDef.CHAT_INFO;
import static com.vk.ai.domain.table.ModelListTableDef.MODEL_LIST;

@RestController
@RequestMapping("/chat")
@CrossOrigin
public class ChatAiController {

    @Autowired
    private AiTemplateStrategyContext aiTemplateStrategyContext;

    @Autowired
    private ModelListService modelListService;


    @Autowired
    private ChatInfoMapper chatInfoMapper;

    @PostMapping(value = "/stream-chat")
    public SseEmitter streamChat(
            @RequestBody GeneralMessageDto messageDto
    ) {
        String modelId = messageDto.getModelId();
        String sessionId = messageDto.getChatSessionId();
        if (null==sessionId ||sessionId.isEmpty())  throw new LeadNewsException("消息错误");

        Long userId = RequestContextUtil.getUserId();

        QueryWrapper where = QueryWrapper.create().where(CHAT_INFO.ID.eq(sessionId).and(CHAT_INFO.DEL.eq(false).and(CHAT_INFO.USER_ID.eq(userId))));
        ChatInfo chatInfo = chatInfoMapper.selectOneByQuery(where);
        if (null==chatInfo)throw new LeadNewsException("消息列表错误");

        if (null==modelId || modelId.isEmpty())  throw new LeadNewsException("模型id不能为空");

        Boolean thinkingEnabled = messageDto.getThinkingEnabled();
        Boolean searchEnabled = messageDto.getSearchEnabled();

        ModelList modelList = modelListService.getOne(QueryWrapper.create().where(
                MODEL_LIST.ID.eq(modelId)
        ).and(MODEL_LIST.STATE.eq(true)).and(MODEL_LIST.DEL.eq(false)));

        if (null==modelList){
            throw new LeadNewsException("错误的模型");
        }

        Integer tokenLimit = modelList.getTokenLimit();
        if (tokenLimit == 0){
            throw new RuntimeException("模型token已耗尽 请切换模型");
        }

        String pertain = modelList.getPertain();

        if (Arrays.stream(AiType.values()).noneMatch(e -> e.name().equals(pertain))) {
            throw new RuntimeException("不支持的ai类型");
        }

        checkCapability(thinkingEnabled, modelList.getIsThink(), "不支持深度思考的模型");
        checkCapability(searchEnabled, modelList.getIsSearch(), "不支持搜索的模型");

        String prompt = messageDto.getPrompt();
        if (prompt.isEmpty()){
            throw new RuntimeException("消息内容不能为空");
        }

        AiTemplate template = aiTemplateStrategyContext.getTemplate(AiType.valueOf(pertain));
        return template.chatMessage(messageDto,modelList);
    }



    @PostMapping(value = "/create")
    public AjaxResult create(
            @RequestBody createChatDto dto
    ) {
        String sessionId = dto.getChatSessionId();
        Long userId = RequestContextUtil.getUserId();
        if (!StringUtils.isEmpty(sessionId)){
            QueryWrapper where = QueryWrapper.create().where(CHAT_INFO.ID.eq(sessionId).and(CHAT_INFO.USER_ID.eq(userId)).and(CHAT_INFO.DEL.eq(false)));
            ChatInfo chatInfo = chatInfoMapper.selectOneByQuery(where);
            return AjaxResult.success(chatInfo);
        }

        LocalDateTime dateTime = LocalDateTime.now();


        ChatInfo chatInfo = new ChatInfo();
        //默认1
        chatInfo.setUserId(userId);
        chatInfo.setUsedToken(0);
        chatInfo.setCurrentMessageId(1);
        chatInfo.setCreatingTime(dateTime);
        chatInfo.setUpdateTime(dateTime);
        chatInfo.setDel(false);

        chatInfoMapper.insert(chatInfo);

        return AjaxResult.success(chatInfo);
    }

    // 通用校验方法 思考和搜索 是否开启
    private void checkCapability(Boolean featureEnabled, Boolean modelSupports, String errorMsg) {
        if (Boolean.TRUE.equals(featureEnabled) && !Boolean.TRUE.equals(modelSupports)) {
            throw new RuntimeException(errorMsg);
        }
    }

    @GetMapping(value = "/upTitle/{id}")
    public AjaxResult upTitle(
            @PathVariable(name="id") String id,
            @RequestParam(name="title") String title
    ) {
        if (StringUtils.isEmpty(id)|| StringUtils.isEmpty(title))
            throw new LeadNewsException("错误的参数");

        Long userId = RequestContextUtil.getUserId();

        QueryWrapper where = QueryWrapper.create().where(CHAT_INFO.ID.eq(id).and(CHAT_INFO.USER_ID.eq(userId)).and(CHAT_INFO.DEL.eq(false)));
        ChatInfo chatInfo = chatInfoMapper.selectOneByQuery(where);
        if (ObjectUtils.isEmpty(chatInfo))
            throw new LeadNewsException("错误的消息");

        ChatInfo info = new ChatInfo();
        info.setUpdateTime(LocalDateTime.now());
        info.setId(id);
        info.setTitle(title);

        chatInfoMapper.update(info);

        return AjaxResult.success();
    }


    @DeleteMapping(value = "/deChat/{id}")
    public AjaxResult deChat(
            @PathVariable(name="id") String id
    ) {
        if (StringUtils.isEmpty(id))
            throw new LeadNewsException("错误的参数");

        Long userId = RequestContextUtil.getUserId();

        QueryWrapper where = QueryWrapper.create().where(CHAT_INFO.ID.eq(id).and(CHAT_INFO.USER_ID.eq(userId)));
        ChatInfo chatInfo = chatInfoMapper.selectOneByQuery(where);

        if (ObjectUtils.isEmpty(chatInfo))
            throw new LeadNewsException("错误的消息");

        Boolean del = chatInfo.getDel();
        if (del)
            throw new LeadNewsException("已经被移除的消息");

        ChatInfo info = new ChatInfo();
        info.setUpdateTime(LocalDateTime.now());
        info.setId(id);
        info.setDel(true);

        chatInfoMapper.update(info);

        return AjaxResult.success();
    }

}
