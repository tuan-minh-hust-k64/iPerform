package com.platform.iperform.service;

import com.platform.iperform.common.dto.request.CommentRequest;
import com.platform.iperform.common.dto.response.CommentResponse;
import com.platform.iperform.common.exception.NotFoundException;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.dataaccess.comment.adapter.CommentRepositoryImpl;
import com.platform.iperform.dataaccess.comment.entity.CommentEntity;
import com.platform.iperform.dataaccess.eks.mapper.EksDataAccessMapper;
import com.platform.iperform.model.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class CommentService {
    private final EksDataAccessMapper eksDataAccessMapper;
    private final CommentRepositoryImpl commentRepository;
    private final FunctionHelper functionHelper;

    public CommentService(EksDataAccessMapper eksDataAccessMapper, CommentRepositoryImpl commentRepository, FunctionHelper functionHelper) {
        this.eksDataAccessMapper = eksDataAccessMapper;
        this.commentRepository = commentRepository;
        this.functionHelper = functionHelper;
    }
    @Transactional(readOnly = true)
    public CommentResponse getCommentByParentId(CommentRequest commentRequest) {
        List<Comment> result = commentRepository.getCommentByParentId(commentRequest.getParentId())
                .orElse(List.of());
        return CommentResponse.builder()
                .comment(result)
                .build();
    }
    @Transactional
    public CommentResponse createComment(CommentRequest commentRequest) {
        Comment result = commentRepository.save(commentRequest.getComment());
        return CommentResponse.builder()
                .comment(List.of(result))
                .build();
    }
    @Transactional
    public CommentResponse updateCommentByUserId(CommentRequest commentRequest, UUID userId) {
        CommentEntity commentEntity = commentRepository.findByIdAndUserId(commentRequest.getComment().getId(), userId)
                .orElseThrow(() -> new NotFoundException("Not Found Comment with id: " + commentRequest.getComment().getId()));
        commentEntity.setLastUpdateAt(ZonedDateTime.now(ZoneId.of("UTC")));
        BeanUtils.copyProperties(
                commentRequest.getComment(),
                commentEntity,
                functionHelper.getNullPropertyNames(commentRequest.getComment())
        );
        Comment result = commentRepository.save(eksDataAccessMapper.commentEntityToComment(commentEntity));
        return CommentResponse.builder()
                .comment(List.of(result))
                .build();
    }
}
