package com.vk.comment.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.UpdateEntity;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.comment.common.utils.CommentUtils;
import com.vk.comment.domain.ApComment;
import com.vk.comment.domain.ApCommentRepay;
import com.vk.comment.domain.dto.CommentReSaveDto;
import com.vk.comment.domain.table.ApCommentRepayTableDef;
import com.vk.comment.mapper.ApCommentMapper;
import com.vk.comment.mapper.ApCommentRepayMapper;
import com.vk.comment.service.ApCommentRepayService;
import com.vk.common.core.constant.DatabaseConstants;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static com.vk.comment.domain.table.ApCommentRepayTableDef.AP_COMMENT_REPAY;

/**
 * APP评论回复信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class ApCommentRepayServiceImpl extends ServiceImpl<ApCommentRepayMapper, ApCommentRepay> implements ApCommentRepayService {

    @Autowired
    private CommentUtils commentUtils;

    @Autowired
    private ApCommentMapper apCommentMapper;

    @Override
    public void saveCommentRe(CommentReSaveDto dto) {
        Long userId = RequestContextUtil.getUserId();
        String userName = RequestContextUtil.getUserName();

        Long commentId = dto.getCommentId();
        Long commentRepayId = dto.getCommentRepayId();

        String content = dto.getContent();
        String image = dto.getImage();

        if (StringUtils.isLongEmpty(commentId)){
            throw new LeadNewsException("评论id不能为空");
        }

        if (StringUtils.isEmpty(content) && StringUtils.isEmpty(image)){
            throw new LeadNewsException("评论内容或回复图片不能为空");
        }

        LocalDateTime dateTime = LocalDateTime.now();

        ApCommentRepay commentRepay = new ApCommentRepay();
        commentRepay.setAuthorId(userId);
        commentRepay.setAuthorName(userName);
        commentRepay.setCommentId(commentId);
        commentRepay.setCommentRepayId(commentRepayId);
        commentRepay.setContent(content);
        commentRepay.setImage(image);
        commentRepay.setCreatedTime(dateTime);
        commentRepay.setUpdatedTime(dateTime);

        mapper.insert(commentRepay);

    }

    @Override
    public void removeCommentRe(Serializable id) {
        Long userId = RequestContextUtil.getUserId();

        ApCommentRepay commentRepay = mapper.selectOneById(id);

        if (null== commentRepay){
            throw new LeadNewsException("评论已被删除");
        }
        ApComment comment = apCommentMapper.selectOneById(commentRepay.getCommentId());

        commentUtils.getaLong(comment.getEntryId(),commentRepay.getAuthorId(),userId);

        ApCommentRepay apCommentRepay = UpdateEntity.of(ApCommentRepay.class,id);
        apCommentRepay.setStatus(DatabaseConstants.DB_ROW_STATUS_NO);

        mapper.update(apCommentRepay);
    }

    @Override
    public Page<ApCommentRepay> getCommentReList(Serializable commentId, Long page, Long size) {

        Page<ApCommentRepay> paginate = mapper.paginate(Page.of(page, size),
                QueryWrapper.create().where(
                        AP_COMMENT_REPAY.COMMENT_ID.eq(commentId)).orderBy(AP_COMMENT_REPAY.CREATED_TIME, false)
        );


        return paginate;
    }
}
