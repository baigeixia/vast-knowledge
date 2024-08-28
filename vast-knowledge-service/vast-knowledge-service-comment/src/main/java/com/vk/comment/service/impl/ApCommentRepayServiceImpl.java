package com.vk.comment.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.UpdateEntity;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.comment.common.utils.CommentUtils;
import com.vk.comment.document.ApCommentDocument;
import com.vk.comment.document.ApCommentRepayDocument;
import com.vk.comment.domain.ApComment;
import com.vk.comment.domain.ApCommentRepay;
import com.vk.comment.domain.dto.CommentReSaveDto;
import com.vk.comment.domain.vo.CommentListRe;
import com.vk.comment.mapper.ApCommentMapper;
import com.vk.comment.mapper.ApCommentRepayMapper;
import com.vk.comment.repository.CommentRepayDocumentRepository;
import com.vk.comment.service.ApCommentRepayService;
import com.vk.common.core.constant.DatabaseConstants;
import com.vk.common.core.domain.R;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.utils.StringUtils;
import com.vk.user.domain.AuthorInfo;
import com.vk.user.feign.RemoteClientUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.vk.comment.common.CommentConstants.COMMENT_TYPE_HOT;
import static com.vk.comment.common.CommentConstants.COMMENT_TYPE_NEW;
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

    @Autowired
    private RemoteClientUserService remoteClientUserService;

    @Autowired
    private CommentRepayDocumentRepository commentRepayDocumentRepository;

    @Override
    public CommentListRe saveCommentRe(CommentReSaveDto dto) {
        Long userId = RequestContextUtil.getUserId();
        String userName = RequestContextUtil.getUserName();

        Long commentId = dto.getCommentId();
        Long commentRepayId = dto.getCommentRepayId();

        String content = dto.getContent();
        String image = dto.getImage();
        Long repayAuthorId = dto.getRepayAuthorId();

        if (StringUtils.isLongEmpty(commentId)) {
            throw new LeadNewsException("评论id不能为空");
        }

        if (StringUtils.isEmpty(content) && StringUtils.isEmpty(image)) {
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
        commentRepay.setRepayAuthorId(repayAuthorId);

        mapper.insert(commentRepay);

        CommentListRe listRe = new CommentListRe();
        listRe.setId(commentRepay.getId());
        listRe.setAuthorId(userId);
        listRe.setTime(dateTime);
        listRe.setCommentId(commentId);
        listRe.setImage(image);
        listRe.setText(content);
        listRe.setLikes(0L);
        listRe.setCommentRepayId(commentRepayId);

        var authorIdRe=0L;

        if (commentRepayId.equals(commentId)){
            ApComment comment = apCommentMapper.selectOneById(commentId);
            if (ObjectUtils.isEmpty(comment)){
                throw new LeadNewsException("回复评论已被删除");
            }
            authorIdRe = comment.getAuthorId();
        }else {
            ApCommentRepay repay = mapper.selectOneById(commentRepayId);
            if (ObjectUtils.isEmpty(repay)){
                throw new LeadNewsException("回复评论已被删除");
            }
            authorIdRe = repay.getAuthorId();
        }

        if (StringUtils.isLongEmpty(authorIdRe)){
            throw new LeadNewsException("回复用户缺失");
        }

        HashSet<Long> longHashSet = new HashSet<>();

        longHashSet.add(userId);
        longHashSet.add(authorIdRe);

        R<Map<Long, AuthorInfo>> userList = remoteClientUserService.getUserList(longHashSet);
        if (StringUtils.isNull(userList) || StringUtils.isNull(userList.getData())) {
            throw new LeadNewsException("错误的用户");
        }

        Map<Long, AuthorInfo> data = userList.getData();

        listRe.setReply(data.get(authorIdRe));
        listRe.setAuthor(data.get(userId));

        ApCommentRepayDocument repayDocument = new ApCommentRepayDocument();
        BeanUtils.copyProperties(commentRepay,repayDocument);
        commentRepayDocumentRepository.save(repayDocument);

        return listRe;
    }

    @Override
    public void removeCommentRe(Serializable id) {
        Long userId = RequestContextUtil.getUserId();

        ApCommentRepay commentRepay = mapper.selectOneById(id);

        if (null == commentRepay) {
            throw new LeadNewsException("评论已被删除");
        }
        ApComment comment = apCommentMapper.selectOneById(commentRepay.getCommentId());

        commentUtils.getaLong(comment.getEntryId(), commentRepay.getAuthorId(), userId);

        ApCommentRepay apCommentRepay = UpdateEntity.of(ApCommentRepay.class, id);
        apCommentRepay.setStatus(DatabaseConstants.DB_ROW_STATUS_NO);

        mapper.update(apCommentRepay);
    }

    @Override
    public List<CommentListRe> getCommentReList(Integer type, Serializable commentId, Long page, Long size) {
        List<CommentListRe> resultList = new ArrayList<>();

        QueryWrapper wrapper = QueryWrapper.create().where(
                AP_COMMENT_REPAY.COMMENT_ID.eq(commentId));

        if (Objects.equals(type, COMMENT_TYPE_HOT)) {
            // 最热
            wrapper.orderBy(AP_COMMENT_REPAY.LIKES, false).orderBy(AP_COMMENT_REPAY.ID, false);
        } else if (Objects.equals(type, COMMENT_TYPE_NEW)) {
            // 最新
            wrapper.orderBy(AP_COMMENT_REPAY.UPDATED_TIME, false);
        }

        Page<ApCommentRepay> paginate = mapper.paginate(Page.of(page, size), wrapper);

        List<ApCommentRepay> records = paginate.getRecords();
        Set<Long> authorId = records.stream().map(ApCommentRepay::getAuthorId).collect(Collectors.toSet());
        Set<Long> commentReList = records.stream().map(ApCommentRepay::getCommentRepayId).collect(Collectors.toSet());

        List<ApCommentRepay> apCommentRepays = mapper.selectListByQuery(QueryWrapper.create().where(AP_COMMENT_REPAY.COMMENT_REPAY_ID.in(commentReList)));
        Set<Long> longSet = apCommentRepays.stream().map(ApCommentRepay::getCommentRepayId).collect(Collectors.toSet());

        authorId.addAll(longSet);
        R<Map<Long, AuthorInfo>> userList = remoteClientUserService.getUserList(authorId);
        if (StringUtils.isNull(userList) || StringUtils.isNull(userList.getData())) {
            throw new LeadNewsException("错误的用户");
        }

        Map<Long, AuthorInfo> userMapData = userList.getData();

        for (ApCommentRepay record : paginate.getRecords()) {

            CommentListRe listRe = new CommentListRe();

            listRe.setText(record.getContent());
            listRe.setId(record.getId());
            listRe.setTime(record.getCreatedTime());
            listRe.setLikes(record.getLikes());
            listRe.setCommentId(record.getCommentId());
            listRe.setImage(record.getImage());
            listRe.setAuthorId(record.getAuthorId());
            listRe.setCommentRepayId(record.getCommentRepayId());

            listRe.setReply(userMapData.get(record.getAuthorId()));
            listRe.setAuthor(userMapData.get(record.getAuthorId()));

            resultList.add(listRe);
        }


        return resultList;
    }
}
