package com.platform.iperform.dataaccess.checkpoint.adapter;

import com.platform.iperform.common.valueobject.CategoryCheckpoint;
import com.platform.iperform.common.valueobject.CheckPointStatus;
import com.platform.iperform.common.valueobject.CommentStatus;
import com.platform.iperform.dataaccess.checkpoint.entity.CheckPointEntity;
import com.platform.iperform.dataaccess.checkpoint.mapper.CheckPointDataAccessMapper;
import com.platform.iperform.dataaccess.checkpoint.repository.CheckPointJpaRepository;
import com.platform.iperform.dataaccess.comment.entity.CommentEntity;
import com.platform.iperform.dataaccess.comment.repository.CommentJpaRepository;
import com.platform.iperform.dataaccess.eks.mapper.EksDataAccessMapper;
import com.platform.iperform.model.CheckPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
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
    public CheckPoint findByUserIdAndTitle(UUID userId, String title) {
        CheckPoint checkPoint = checkPointDataAccessMapper.checkPointEntityToCheckPoint(
                checkPointJpaRepository.findByUserIdAndTitle(userId, title).orElse(CheckPointEntity.builder()
                        .status(CheckPointStatus.INIT).build())
        );

        checkPoint.getCheckPointItems().forEach(checkPointItem -> {
            List<CommentEntity> commentCheckPointItem = commentJpaRepository.findByParentIdAndStatusOrderByCreatedAtAsc(checkPointItem.getId(), CommentStatus.INIT)
                    .orElse(List.of());
            checkPointItem.setComments(eksDataAccessMapper.commentEntitiesToComments(commentCheckPointItem));
        });
        return checkPoint;
    }

    public CheckPoint findByTitleAndUserIdAndCategory(UUID userId, String title, String category) {
        String categoryFilter = category; 
        if (category == null) {
            categoryFilter = CategoryCheckpoint.NORMAL.toString();
        }

        Optional<CheckPointEntity> checkpointRecord = checkPointJpaRepository
                .findByTitleAndUserIdAndCategory(title, userId, CategoryCheckpoint.valueOf(categoryFilter));

        CheckPoint checkPoint = checkPointDataAccessMapper.checkPointEntityToCheckPoint(
                checkpointRecord.orElse(CheckPointEntity.builder()
                        .status(CheckPointStatus.INIT).build())
        );

        checkPoint.getCheckPointItems().forEach(checkPointItem -> {
            List<CommentEntity> commentCheckPointItem = commentJpaRepository.findByParentIdAndStatusOrderByCreatedAtAsc(checkPointItem.getId(), CommentStatus.INIT)
                    .orElse(List.of());
            checkPointItem.setComments(eksDataAccessMapper.commentEntitiesToComments(commentCheckPointItem));
        });
        return checkPoint;
    }
}
