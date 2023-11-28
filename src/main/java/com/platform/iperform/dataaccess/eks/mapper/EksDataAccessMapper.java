package com.platform.iperform.dataaccess.eks.mapper;

import com.platform.iperform.dataaccess.comment.entity.CommentEntity;
import com.platform.iperform.dataaccess.eks.entity.EksEntity;
import com.platform.iperform.dataaccess.eks.entity.KeyStepEntity;
import com.platform.iperform.model.Comment;
import com.platform.iperform.model.Eks;
import com.platform.iperform.model.KeyStep;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class EksDataAccessMapper {
    public Eks eksEntityToEks(EksEntity eksEntity) {
        return Eks.builder()
                .id(eksEntity.getId())
                .content(eksEntity.getContent())
                .comments(commentEntitiesToComments(eksEntity.getCommentEntities()))
                .createdAt(eksEntity.getCreatedAt())
                .type(eksEntity.getType())
                .lastUpdateAt(eksEntity.getLastUpdateAt())
                .ordinalNumber(eksEntity.getOrdinalNumber())
                .process(eksEntity.getProcess())
                .userId(eksEntity.getUserId())
                .timePeriod(eksEntity.getTimePeriod())
                .keySteps(keyStepEntitiesToKeySteps(eksEntity.getKeyStepEntities()))
                .status(eksEntity.getStatus())
                .build();

    }

    public EksEntity eksToEksEntity(Eks eks) {
        EksEntity eksEntity = EksEntity.builder()
                .content(eks.getContent())
                .ordinalNumber(eks.getOrdinalNumber())
                .type(eks.getType())
                .process(eks.getProcess())
                .status(eks.getStatus())
                .timePeriod(eks.getTimePeriod())
                .userId(eks.getUserId())
                .build();
        if(eks.getId() == null) {
            eksEntity.setId(UUID.randomUUID());
            eksEntity.setCreatedAt(ZonedDateTime.now(ZoneId.of("UTC")));
            eksEntity.setLastUpdateAt(ZonedDateTime.now(ZoneId.of("UTC")));
        } else {
            eksEntity.setId(eks.getId());
            eksEntity.setCreatedAt(eks.getCreatedAt());
            eksEntity.setLastUpdateAt(eks.getLastUpdateAt());
        }
        return eksEntity;
    }

    public Comment commentEntityToComment(CommentEntity commentEntity) {
        return Comment.builder()
                .content(commentEntity.getContent())
                .type(commentEntity.getType())
                .userId(commentEntity.getUserId())
                .lastUpdateAt(commentEntity.getLastUpdateAt())
                .createdAt(commentEntity.getCreatedAt())
                .parentId(commentEntity.getParent().getId())
                .id(commentEntity.getId())
                .build();
    }

    public CommentEntity commentToCommentEntity(Comment comment) {
        return CommentEntity.builder()
                .type(comment.getType())
                .userId(comment.getUserId())
                .id(comment.getId())
                .content(comment.getContent())
                .lastUpdateAt(comment.getLastUpdateAt())
                .createdAt(comment.getCreatedAt())
                .build();
    }
    public List<KeyStepEntity> keyStepsToKeyStepEntities(List<KeyStep> keySteps) {
        return keySteps.stream().map(keyStep -> KeyStepEntity.builder()
                .id(keyStep.getId())
                .ordinalNumber(keyStep.getOrdinalNumber())
                .status(keyStep.getStatus())
                .lastUpdateAt(keyStep.getLastUpdateAt())
                .createdAt(keyStep.getCreatedAt())
                .content(keyStep.getContent())
                .build()).toList();
    }

    public List<CommentEntity> commentsToCommentEntities(List<Comment> comments) {
        return comments.stream().map(comment -> CommentEntity.builder()
                .type(comment.getType())
                .userId(comment.getUserId())
                .id(comment.getId())
                .content(comment.getContent())
                .lastUpdateAt(comment.getLastUpdateAt())
                .createdAt(comment.getCreatedAt())
                .build()).toList();
    }

    public List<KeyStep> keyStepEntitiesToKeySteps(List<KeyStepEntity> keyStepEntities) {
        if(keyStepEntities == null) return List.of();
        return keyStepEntities.stream().map(keyStepEntity -> KeyStep.builder()
                .lastUpdateAt(keyStepEntity.getLastUpdateAt())
                .status(keyStepEntity.getStatus())
                .content(keyStepEntity.getContent())
                .createdAt(keyStepEntity.getCreatedAt())
                .eId(keyStepEntity.getEks().getId())
                .ordinalNumber(keyStepEntity.getOrdinalNumber())
                .build()).toList();
    }

    public List<Comment> commentEntitiesToComments(List<CommentEntity> commentEntities) {
        if(commentEntities == null) return List.of();
        return commentEntities.stream().map(commentEntity -> Comment.builder()
                .content(commentEntity.getContent())
                .type(commentEntity.getType())
                .userId(commentEntity.getUserId())
                .lastUpdateAt(commentEntity.getLastUpdateAt())
                .createdAt(commentEntity.getCreatedAt())
                .parentId(commentEntity.getParent().getId())
                .id(commentEntity.getId())
                .build()).toList();
    }
}
