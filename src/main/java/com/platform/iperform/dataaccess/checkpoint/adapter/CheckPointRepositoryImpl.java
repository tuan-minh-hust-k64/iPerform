package com.platform.iperform.dataaccess.checkpoint.adapter;

import com.platform.iperform.common.valueobject.CommentStatus;
import com.platform.iperform.dataaccess.checkpoint.entity.CheckPointEntity;
import com.platform.iperform.dataaccess.checkpoint.mapper.CheckPointDataAccessMapper;
import com.platform.iperform.dataaccess.checkpoint.repository.CheckPointJpaRepository;
import com.platform.iperform.dataaccess.comment.entity.CommentEntity;
import com.platform.iperform.dataaccess.comment.repository.CommentJpaRepository;
import com.platform.iperform.dataaccess.eks.mapper.EksDataAccessMapper;
import com.platform.iperform.model.CheckPoint;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CheckPointRepositoryImpl {
    private final CheckPointJpaRepository checkPointJpaRepository;
    private final CommentJpaRepository commentJpaRepository;
    private final CheckPointDataAccessMapper checkPointDataAccessMapper;
    private final EksDataAccessMapper eksDataAccessMapper;

    public CheckPointRepositoryImpl(CheckPointJpaRepository checkPointJpaRepository, CommentJpaRepository commentJpaRepository,
                                    CheckPointDataAccessMapper checkPointDataAccessMapper, EksDataAccessMapper eksDataAccessMapper) {
        this.checkPointJpaRepository = checkPointJpaRepository;
        this.commentJpaRepository = commentJpaRepository;
        this.checkPointDataAccessMapper = checkPointDataAccessMapper;
        this.eksDataAccessMapper = eksDataAccessMapper;
    }

    public List<CheckPoint> getCheckPointByUserId(UUID userId) {
        List<CheckPoint> result = checkPointJpaRepository.findByUserIdOrderByCreatedAtDesc(
                userId
        ).stream().map(checkPointDataAccessMapper::checkPointEntityToCheckPoint).toList();
        result.forEach(item -> {
            List<CommentEntity> comments = commentJpaRepository.findByParentIdAndStatusOrderByCreatedAtAsc(item.getId(), CommentStatus.INIT)
                    .orElse(List.of());
            item.setComments(eksDataAccessMapper.commentEntitiesToComments(comments));
            item.getCheckPointItems().forEach(checkPointItem -> {
                List<CommentEntity> commentCheckPointItem = commentJpaRepository.findByParentIdAndStatusOrderByCreatedAtAsc(checkPointItem.getId(), CommentStatus.INIT)
                        .orElse(List.of());
                checkPointItem.setComments(eksDataAccessMapper.commentEntitiesToComments(commentCheckPointItem));
            });
        });
        return result;
    }

    public CheckPointEntity save(CheckPointEntity checkPointEntity) {
        return checkPointJpaRepository.save(
                       checkPointEntity
        );
    }

    public Optional<CheckPointEntity> findById(UUID id) {
        return checkPointJpaRepository.findById(id);
    }
    public Optional<CheckPointEntity> findByIdAndUserId(UUID id, UUID userId) {
        return checkPointJpaRepository.findByIdAndUserId(id, userId);
    }
    public Optional<CheckPointEntity> findByUserIdAndTitle(UUID userId, String title) {
        return checkPointJpaRepository.findByUserIdAndTitle(userId, title);
    }
}
