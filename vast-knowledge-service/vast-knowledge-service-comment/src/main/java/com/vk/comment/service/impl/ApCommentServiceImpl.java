package com.vk.comment.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.util.UpdateEntity;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.comment.common.CommentConstants;
import com.vk.comment.common.utils.CommentUtils;
import com.vk.comment.document.ApCommentDocument;
import com.vk.comment.domain.ApComment;
import com.vk.comment.domain.ApCommentRepay;
import com.vk.comment.domain.dto.CommentSaveDto;
import com.vk.comment.domain.dto.UpCommentDto;
import com.vk.comment.domain.vo.CommentList;
import com.vk.comment.domain.vo.CommentListRe;
import com.vk.comment.domain.vo.CommentListVo;
import com.vk.comment.mapper.ApCommentMapper;
import com.vk.comment.mapper.ApCommentRepayMapper;
import com.vk.comment.repository.CommentDocumentRepository;
import com.vk.comment.service.ApCommentService;
import com.vk.common.core.constant.DatabaseConstants;
import com.vk.common.core.domain.R;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.utils.StringUtils;
import com.vk.user.domain.AuthorInfo;
import com.vk.user.feign.RemoteClientUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.vk.comment.common.CommentConstants.COMMENT_TYPE_HOT;
import static com.vk.comment.common.CommentConstants.COMMENT_TYPE_NEW;
import static com.vk.comment.domain.table.ApCommentRepayTableDef.AP_COMMENT_REPAY;
import static com.vk.comment.domain.table.ApCommentTableDef.AP_COMMENT;

/**
 * APP评论信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
@Slf4j(topic = "CommentService")
public class ApCommentServiceImpl extends ServiceImpl<ApCommentMapper, ApComment> implements ApCommentService {

    @Autowired
    private CommentUtils commentUtils;

    @Autowired
    private ApCommentRepayMapper apCommentRepayMapper;

    @Autowired
    private RemoteClientUserService remoteClientUserService;

    @Autowired
    private CommentDocumentRepository commentDocumentRepository;

    @Override
    public CommentList saveComment(CommentSaveDto dto) {
        Long userId = RequestContextUtil.getUserId();
        String userName = RequestContextUtil.getUserName();

        LocalDateTime dateTime = LocalDateTime.now();

        Long entryId = dto.getEntryId();
        if (StringUtils.isLongEmpty(entryId)) {
            throw new RuntimeException("文章错误");
        }

        String content = dto.getContent();
        String image = dto.getImage();
        if (StringUtils.isEmpty(content) && StringUtils.isEmpty(image)) {
            throw new RuntimeException("评论内容不能为空");
        }

        ApComment comment = new ApComment();
        comment.setUpdatedTime(dateTime);
        comment.setCreatedTime(dateTime);
        comment.setFlag(CommentConstants.FLAG_TYPE_ORDINARY);
        comment.setLikes(0L);
        comment.setReply(0L);
        comment.setImage(image);
        comment.setContent(content);
        comment.setType(dto.getType());
        comment.setChannelId(dto.getChannelId());
        comment.setEntryId(dto.getEntryId());
        comment.setAuthorId(userId);
        comment.setAuthorName(userName);

        mapper.insert(comment);

        CommentList list = new CommentList();
        list.setId(comment.getId());
        list.setImage(comment.getImage());
        list.setTime(comment.getCreatedTime());
        list.setLikes(comment.getLikes());
        list.setText(comment.getContent());
        list.setChildCommentCount(0L);
        list.setAuthorId(userId);

        R<Map<Long, AuthorInfo>> userList = remoteClientUserService.getUserList(Set.of(userId));
        if (StringUtils.isNull(userList) || StringUtils.isNull(userList.getData())) {
            throw new LeadNewsException("错误的用户");
        }
        Map<Long, AuthorInfo> data = userList.getData();
        list.setAuthor(data.get(userId));

        ApCommentDocument commentDocument = new ApCommentDocument();
        BeanUtils.copyProperties(comment,commentDocument);
        commentDocumentRepository.save(commentDocument);

        return list;
    }

    @Override
    public void removeComment(Serializable id) {
        Long userId = RequestContextUtil.getUserId();
        ApComment comment = mapper.selectOneById(id);

        if (null == comment) {
            throw new LeadNewsException("评论不存在");
        }
        Long authorId = comment.getAuthorId();

        // 检查是否是 文章作者 或者 是发布人
        commentUtils.getaLong(comment.getEntryId(), authorId, userId);

        ApComment upCom = UpdateEntity.of(ApComment.class, comment.getId());
        upCom.setStatus(DatabaseConstants.DB_ROW_STATUS_NO);

        mapper.update(comment);
    }

    @Override
    public void updateComment(UpCommentDto dto) {
        Long userId = RequestContextUtil.getUserId();

        Long commentId = dto.getCommentId();
        if (StringUtils.isLongEmpty(commentId)) {
            throw new LeadNewsException("错误的评论");
        }
        Long entryId = dto.getEntryId();
        if (StringUtils.isLongEmpty(entryId)) {
            throw new LeadNewsException("错误的文章id");
        }

        Long commentRepayId = dto.getCommentRepayId();
        // 是否是子级修改
        if (StringUtils.isLongEmpty(commentRepayId)) {
            // 子级修改 不考虑子级置顶问题
            ApCommentRepay commentRepay = apCommentRepayMapper.selectOneByQuery(
                    QueryWrapper.create().where(AP_COMMENT_REPAY.COMMENT_ID.eq(commentId)
                            .and(AP_COMMENT_REPAY.COMMENT_REPAY_ID.eq(commentRepayId)))
            );

            if (null == commentRepay) {
                throw new LeadNewsException("已被删除的评论");
            }

            Long authorId = commentRepay.getAuthorId();

            // 检查是否是 文章作者 或者 是发布人
            commentUtils.getaLong(entryId, authorId, userId);

            //  平级 评论 --  顶级父-》 父-》子
            ApCommentRepay upComRe = UpdateEntity.of(ApCommentRepay.class, commentRepay.getId());
            upComRe.setStatus(DatabaseConstants.DB_ROW_STATUS_NO);

            apCommentRepayMapper.update(upComRe);

        } else {
            ApComment comment = mapper.selectOneById(commentId);
            if (null == comment) {
                throw new LeadNewsException("已被删除的评论");
            }

            Long authorId = comment.getAuthorId();

            Long articleAuthorId = commentUtils.getaLong(entryId, authorId, userId);

            Integer flag = dto.getFlag();
            Integer status = dto.getStatus();

            if (ObjectUtils.isEmpty(flag) && ObjectUtils.isEmpty(status)) {
                throw new LeadNewsException("参数不能为空");
            }

            ApComment upCom = UpdateEntity.of(ApComment.class, commentId);

            if (null != flag) {
                if (!Objects.equals(authorId, articleAuthorId)) {
                    throw new LeadNewsException("错误的文章作者");
                }
                // 只能推荐或者置顶
                if (CommentConstants.FLAG_TYPE_RECOMMEND.equals(flag) || CommentConstants.FLAG_TYPE_BEST.equals(flag)) {
                    upCom.setFlag(flag);
                } else {
                    throw new LeadNewsException("错误的操作");
                }
            }

            if (null != status) {
                if (Objects.equals(DatabaseConstants.DB_ROW_STATUS_YES, status)) {
                    throw new LeadNewsException("错误的操作");
                }
                upCom.setStatus(DatabaseConstants.DB_ROW_STATUS_NO);
            }

            Db.tx(() -> {
                mapper.update(upCom);
                // 顶级父级改变  子级移除
                mapper.upAllComment(commentId, DatabaseConstants.DB_ROW_STATUS_NO);
                return true;
            });
        }


    }

    @Override
    public CommentListVo getCommentList(Serializable entryId, Integer type, Long page, Long size) {

        CommentListVo result = new CommentListVo();
        result.setPage(page);
        result.setSize(size);

        QueryWrapper commentWhere = QueryWrapper.create().where(AP_COMMENT.ENTRY_ID.eq(entryId)
                .and(AP_COMMENT.STATUS.eq(DatabaseConstants.DB_ROW_STATUS_YES)));

        if (Objects.equals(type, COMMENT_TYPE_HOT)) {
            // 最热
            commentWhere.orderBy(AP_COMMENT.LIKES, false).orderBy(AP_COMMENT.ID,false);
        } else if (Objects.equals(type, COMMENT_TYPE_NEW)) {
            // 最新
            commentWhere.orderBy(AP_COMMENT.UPDATED_TIME, false);
        }

        // 顶级父级分页
        Page<ApComment> dbCommentPage = mapper.paginate(Page.of(page, size), commentWhere);

        List<ApComment> dbCommentList = dbCommentPage.getRecords();

        result.setTotal(dbCommentPage.getTotalRow());

        if (CollectionUtils.isEmpty(dbCommentList)) {
            return result;
        }
        // 用户id合集
        Set<Long> authorIdSet = dbCommentList.stream().map(ApComment::getAuthorId).collect(Collectors.toSet());
        //评论集合
        List<CommentList> comments = result.getComments();
        //子评论用户 map合集
        var  idMapAuthorId = new  HashMap<Long,Long>();
        // 聚合
        for (ApComment comment : dbCommentList) {
            CommentList commentList = new CommentList();
            commentList.setId(comment.getId());
            commentList.setText(comment.getContent());
            commentList.setImage(comment.getImage());
            commentList.setLikes(comment.getLikes());
            commentList.setTime(comment.getCreatedTime());
            commentList.setAuthorId(comment.getAuthorId());
            // 构建子级
            QueryWrapper wrapper = QueryWrapper.create().where(
                    AP_COMMENT_REPAY.COMMENT_ID.eq(comment.getId()));

            if (Objects.equals(type, COMMENT_TYPE_HOT)) {
                // 最热
                wrapper.orderBy(AP_COMMENT_REPAY.LIKES, false).orderBy(AP_COMMENT_REPAY.ID,false);
            } else if (Objects.equals(type, COMMENT_TYPE_NEW)) {
                // 最新
                wrapper.orderBy(AP_COMMENT_REPAY.UPDATED_TIME, false);
            }

            Page<ApCommentRepay> commentRepayPage = apCommentRepayMapper.paginate(Page.of(1, 5),wrapper);

            commentList.setChildCommentCount(commentRepayPage.getTotalRow());

            List<ApCommentRepay> repayList = commentRepayPage.getRecords();
            List<CommentListRe> commentListRes = commentList.getChildComments();

            for (ApCommentRepay commentRepay : repayList) {
                CommentListRe listRe = new CommentListRe();

                listRe.setText(commentRepay.getContent());
                listRe.setId(commentRepay.getId());
                listRe.setCommentId(commentRepay.getCommentId());
                listRe.setTime(commentRepay.getCreatedTime());
                listRe.setLikes(commentRepay.getLikes());
                listRe.setImage(commentRepay.getImage());
                listRe.setAuthorId(commentRepay.getAuthorId());

                listRe.setCommentRepayId(commentRepay.getCommentRepayId());

                commentListRes.add(listRe);
            }

            idMapAuthorId.putAll(repayList.stream().collect(Collectors.toMap(ApCommentRepay::getId, ApCommentRepay::getAuthorId)));
            authorIdSet.addAll(Set.copyOf(idMapAuthorId.values()));
            comments.add(commentList);
        }


        R<Map<Long, AuthorInfo>> userList = remoteClientUserService.getUserList(authorIdSet);
        if (StringUtils.isNull(userList) || StringUtils.isNull(userList.getData())) {
            throw new LeadNewsException("错误的用户");
        }
        Map<Long, AuthorInfo> userMap = userList.getData();
        for (CommentList comment : comments) {
            comment.setAuthor(userMap.get(comment.getAuthorId()));
            List<CommentListRe> childComments = comment.getChildComments();
            for (CommentListRe childComment : childComments) {
                childComment.setAuthor(userMap.get(childComment.getAuthorId()));
                Long commentRepayId = childComment.getCommentRepayId();
                if (!StringUtils.isLongEmpty(commentRepayId)){
                    childComment.setReply(userMap.get(idMapAuthorId.get(commentRepayId)));
                }
            }
        }

        idMapAuthorId.clear();
        userMap.clear();
        authorIdSet.clear();

        return result;
    }




}
