package com.vk.ai.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.ai.domain.ChatInfo;
import com.vk.ai.domain.vo.AiMgListVo;
import com.vk.ai.domain.vo.ChatInfoVo;
import com.vk.ai.domain.vo.PageVo;
import com.vk.ai.mapper.ChatInfoMapper;
import com.vk.ai.service.ChatInfoService;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.web.domain.BaseEntity;
import com.vk.db.domain.aiMessage.AiMg;
import com.vk.db.repository.aiMessage.AiMgRepository;
import io.micrometer.core.instrument.binder.BaseUnits;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.vk.ai.domain.table.ChatInfoTableDef.CHAT_INFO;

/**
 * 消息详情 服务层实现。
 *
 * @author 张三
 * @since 2025-04-17
 */
@Service
public class ChatInfoServiceImpl extends ServiceImpl<ChatInfoMapper, ChatInfo> implements ChatInfoService {

    @Autowired
    private AiMgRepository aiMgRepository;

    @Override
    public Page<ChatInfoVo> getUserList(Integer offset, Integer limit) {
        QueryWrapper wrapper = QueryWrapper.create();
        wrapper.where(CHAT_INFO.DEL.eq(false)).orderBy(CHAT_INFO.UPDATE_TIME,false);
        return mapper.paginateAs(Page.of(offset, limit), wrapper, ChatInfoVo.class);
    }

    @Override
    public  PageVo<AiMgListVo> conversation(String id, Integer offset, Integer limit) {
        if (StringUtils.isEmpty(id)){
            throw  new LeadNewsException("错误的id");
        }

        QueryWrapper where = QueryWrapper.create().where(CHAT_INFO.ID.eq(id).and(CHAT_INFO.DEL.eq(false)));
        ChatInfoVo chatInfoVo = mapper.selectOneByQueryAs(where, ChatInfoVo.class);
        if (null==chatInfoVo){
            throw  new LeadNewsException("对话不存在");
        }
        Pageable pageable = PageRequest.of(offset, limit);
        Slice<AiMg> timeDesc = aiMgRepository.findByInfoIdAndDelFalseOrderByMessageIdDesc(id, pageable);
        List<AiMgListVo> voList = timeDesc.getContent()
                .stream()
                .sorted(Comparator.comparing(AiMg::getMessageId)) // 正序
                .map(this::convertToVo)
                .toList();

        PageVo<AiMgListVo> result = new PageVo<>();
        result.setInfoData(chatInfoVo);
        result.setContent(voList);
        result.setHasNext(timeDesc.hasNext());
        result.setPageNumber(pageable.getPageNumber());
        result.setPageSize(pageable.getPageSize());

        return result;
    }

    private AiMgListVo convertToVo(AiMg entity) {
        AiMgListVo vo = new AiMgListVo();
        BeanUtils.copyProperties(entity,vo);
        return vo;
    }
}
