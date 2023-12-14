package com.platform.iperform.dataaccess.comment.adapter;

import com.platform.iperform.common.exception.NotFoundException;
import com.platform.iperform.common.valueobject.CommentStatus;
import com.platform.iperform.dataaccess.comment.entity.CommentEntity;
import com.platform.iperform.dataaccess.comment.repository.CommentJpaRepository;
import com.platform.iperform.dataaccess.eks.mapper.EksDataAccessMapper;
import com.platform.iperform.model.Comment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CommentRepositoryImpl {
    public final EksDataAccessMapper eksDataAccessMapper;
    public final CommentJpaRepository commentJpaRepository;

    public CommentRepositoryImpl(EksDataAccessMapper eksDataAccessMapper, CommentJpaRepository commentJpaRepository) {
        this.eksDataAccessMapper = eksDataAccessMapper;
        this.commentJpaRepository = commentJpaRepository;
    }

    public Optional<List<Comment>> getCommentByParentId(UUID parentId) {
        return Optional.of(
                eksDataAccessMapper.commentEntitiesToComments(
                        commentJpaRepository.findByParentIdAndStatusOrderByCreatedAtAsc(parentId, CommentStatus.INIT)
                                .orElseThrow(NotFoundException::new)
                )
        );
    }
    public Comment save(Comment comment) {
        return eksDataAccessMapper.commentEntityToComment(
                commentJpaRepository.save(eksDataAccessMapper.commentToCommentEntity(comment))
        );
    }
    public Optional<CommentEntity> findById(UUID id) {
        return commentJpaRepository.findById(id);
    }

    public Optional<CommentEntity> findByIdAndUserId(UUID id, UUID userId) { return commentJpaRepository.findByIdAndUserId(id, userId); }
}
