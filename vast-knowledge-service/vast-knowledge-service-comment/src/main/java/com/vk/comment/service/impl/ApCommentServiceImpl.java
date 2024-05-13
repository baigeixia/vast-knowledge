package com.vk.comment.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.comment.domain.ApComment;
import com.vk.comment.mapper.ApCommentMapper;
import com.vk.comment.service.ApCommentService;
import org.springframework.stereotype.Service;

/**
 * APP评论信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class ApCommentServiceImpl extends ServiceImpl<ApCommentMapper, ApComment> implements ApCommentService {

}
