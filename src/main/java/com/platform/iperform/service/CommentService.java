package com.platform.iperform.service;

import com.platform.iperform.common.dto.CommentRequest;
import com.platform.iperform.common.dto.CommentResponse;
import com.platform.iperform.common.exception.CommentNotFoundException;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.dataaccess.comment.adapter.CommentRepositoryImpl;
import com.platform.iperform.dataaccess.comment.entity.CommentEntity;
import com.platform.iperform.dataaccess.eks.mapper.EksDataAccessMapper;
import com.platform.iperform.model.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public CommentResponse save(CommentRequest commentRequest) {
        Comment result = commentRepository.save(commentRequest.getComment());
        return CommentResponse.builder()
                .comment(List.of(result))
                .build();
    }
    @Transactional
    public CommentResponse updateComment(CommentRequest commentRequest) {
        CommentEntity commentEntity = commentRepository.findById(commentRequest.getComment().getId())
                .orElseThrow(() -> new CommentNotFoundException("Not Found Comment with id: " + commentRequest.getComment().getId()));
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
