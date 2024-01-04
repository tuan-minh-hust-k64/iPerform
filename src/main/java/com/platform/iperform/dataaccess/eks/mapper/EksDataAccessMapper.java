package com.platform.iperform.dataaccess.eks.mapper;

import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.common.valueobject.BaseEntityAllowComment;
import com.platform.iperform.common.valueobject.CheckInStatus;
import com.platform.iperform.common.valueobject.CommentStatus;
import com.platform.iperform.dataaccess.comment.entity.CommentEntity;
import com.platform.iperform.dataaccess.comment.entity.QuestionEntity;
import com.platform.iperform.dataaccess.comment.mapper.QuestionDataMapper;
import com.platform.iperform.dataaccess.eks.entity.CheckInEntity;
import com.platform.iperform.dataaccess.eks.entity.EksEntity;
import com.platform.iperform.dataaccess.eks.entity.KeyStepEntity;
import com.platform.iperform.model.CheckIn;
import com.platform.iperform.model.Comment;
import com.platform.iperform.model.Eks;
import com.platform.iperform.model.KeyStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class EksDataAccessMapper {
    private final QuestionDataMapper questionDataMapper;
    private final FunctionHelper functionHelper;
    public EksDataAccessMapper(QuestionDataMapper questionDataMapper, FunctionHelper functionHelper) {
        this.questionDataMapper = questionDataMapper;
        this.functionHelper = functionHelper;
    }

    public Eks eksEntityToEks(EksEntity eksEntity) {
        return Eks.builder()
                .id(eksEntity.getId())
                .content(eksEntity.getContent())
                .description(eksEntity.getDescription())
//                .comments(commentEntitiesToComments(eksEntity.getCommentEntities()))
                .createdAt(eksEntity.getCreatedAt())
                .type(eksEntity.getType())
                .lastUpdateAt(eksEntity.getLastUpdateAt())
                .ordinalNumber(eksEntity.getOrdinalNumber())
                .process(eksEntity.getProcess())
                .userId(eksEntity.getUserId())
                .timePeriod(eksEntity.getTimePeriod())
                .keySteps(keyStepEntitiesToKeySteps(eksEntity.getKeyStepEntities()))
                .status(eksEntity.getStatus())
                .checkIns(checkInEntitiesToCheckIn(eksEntity.getCheckInEntities()))
                .build();

    }

    public List<CheckIn> checkInEntitiesToCheckIn(List<CheckInEntity> checkInEntities) {
        if(checkInEntities == null) return List.of();
        return checkInEntities.stream().map(item -> CheckIn.builder()
                .id(item.getId())
                .type(item.getType())
                .status(item.getStatus())
                .eksId(item.getEks().getId())
                .content(item.getContent())
                .createdAt(item.getCreatedAt())
                .lastUpdateAt(item.getLastUpdateAt())
                .progress(item.getProgress())
//                .comments(commentEntitiesToComments(item.getCommentEntities()))
                .build()).toList();
    }

    public EksEntity eksToEksEntity(Eks eks) {
        EksEntity eksEntity = EksEntity.builder()
                .content(eks.getContent())
                .description(eks.getDescription())
                .ordinalNumber(eks.getOrdinalNumber())
                .type(eks.getType())
                .process(eks.getProcess())
                .status(eks.getStatus())
                .timePeriod(eks.getTimePeriod())
                .userId(eks.getUserId())
                .keyStepEntities(keyStepsToKeyStepEntities(eks.getKeySteps()))
                .build();
        eksEntity.getKeyStepEntities().forEach(item -> {
            item.setEks(eksEntity);
        });
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
                .name(commentEntity.getName())
                .parentId(commentEntity.getParentId())
                .id(commentEntity.getId())
                .status(commentEntity.getStatus())
                .questionId(commentEntity.getQuestion() == null? null:commentEntity.getQuestion().getId())
                .build();
    }

    public CommentEntity commentToCommentEntity(Comment comment) {
        CommentEntity commentEntity = CommentEntity.builder()
                .status(comment.getStatus())
                .type(comment.getType())
                .userId(comment.getUserId())
                .id(comment.getId())
                .name(comment.getName())
                .content(comment.getContent())
                .lastUpdateAt(comment.getLastUpdateAt())
                .createdAt(comment.getCreatedAt())
                .parentId(comment.getParentId())
                .build();
        if(comment.getQuestionId() != null) {
            commentEntity.setQuestion(new QuestionEntity(comment.getQuestionId()));
        }
        if(comment.getId() == null) {
            commentEntity.setId(UUID.randomUUID());
            commentEntity.setCreatedAt(ZonedDateTime.now(ZoneId.of("UTC")));
            commentEntity.setLastUpdateAt(ZonedDateTime.now(ZoneId.of("UTC")));
            commentEntity.setStatus(CommentStatus.INIT);
        } else {
            commentEntity.setLastUpdateAt(ZonedDateTime.now(ZoneId.of("UTC")));
        }
        return commentEntity;
    }
    public List<KeyStepEntity> keyStepsToKeyStepEntities(List<KeyStep> keySteps) {
        if(keySteps == null) return List.of();
        return keySteps.stream().map(keyStep -> {
            KeyStepEntity keyStepEntity = KeyStepEntity.builder()
                    .ordinalNumber(keyStep.getOrdinalNumber())
                    .status(keyStep.getStatus())
                    .lastUpdateAt(functionHelper.getZoneDateTime(keyStep.getLastUpdateAt()))
                    .createdAt(functionHelper.getZoneDateTime(keyStep.getCreatedAt()))
                    .content(keyStep.getContent())
                    .eks(new EksEntity(keyStep.getEksId()))
                    .build();
            if(keyStep.getId() == null) {
                keyStepEntity.setId(UUID.randomUUID());
            }
            else keyStepEntity.setId(keyStep.getId());
            return keyStepEntity;
        }).toList();
    }

    public List<CommentEntity> commentsToCommentEntities(List<Comment> comments) {
        if(comments == null) return List.of();
        return comments.stream().map(comment -> {
            CommentEntity commentEntity = CommentEntity.builder()
                    .type(comment.getType())
                    .userId(comment.getUserId())
                    .name(comment.getName())
                    .parentId(comment.getParentId())
                    .status(comment.getStatus())
                    .content(comment.getContent())
                    .lastUpdateAt(comment.getLastUpdateAt())
                    .createdAt(comment.getCreatedAt())
                    .build();
            if(comment.getId() == null) {
                commentEntity.setId(UUID.randomUUID());
                commentEntity.setQuestion(new QuestionEntity(comment.getQuestionId()));
                commentEntity.setCreatedAt(ZonedDateTime.now(ZoneId.of("UTC")));
                commentEntity.setLastUpdateAt(ZonedDateTime.now(ZoneId.of("UTC")));
            }
            else commentEntity.setId(comment.getId());

            return commentEntity;
        }).toList();
    }

    public List<KeyStep> keyStepEntitiesToKeySteps(List<KeyStepEntity> keyStepEntities) {
        if(keyStepEntities == null) return List.of();
        return keyStepEntities.stream().map(keyStepEntity -> KeyStep.builder()
                .id(keyStepEntity.getId())
                .lastUpdateAt(keyStepEntity.getLastUpdateAt())
                .status(keyStepEntity.getStatus())
                .content(keyStepEntity.getContent())
                .createdAt(keyStepEntity.getCreatedAt())
                .eksId(keyStepEntity.getEks().getId())
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
                .name(commentEntity.getName())
                .parentId(commentEntity.getParentId())
                .id(commentEntity.getId())
                .status(commentEntity.getStatus())
                .questionId(commentEntity.getQuestion() == null? null:commentEntity.getQuestion().getId())
                .build()).toList();
    }

    public CheckInEntity checkInToCheckInEntity(CheckIn checkIn) {
        CheckInEntity checkInEntity = CheckInEntity.builder()
                .eks(new EksEntity(checkIn.getEksId()))
                .status(checkIn.getStatus())
                .type(checkIn.getType())
                .lastUpdateAt(checkIn.getLastUpdateAt())
                .createdAt(checkIn.getCreatedAt())
                .content(checkIn.getContent())
                .progress(checkIn.getProgress())
                .build();
        if(checkIn.getId() == null) {
            checkInEntity.setId(UUID.randomUUID());
            checkInEntity.setStatus(CheckInStatus.INIT);
            checkInEntity.setCreatedAt(ZonedDateTime.now(ZoneId.of("UTC")));
            checkInEntity.setLastUpdateAt(ZonedDateTime.now(ZoneId.of("UTC")));
        } else {
            checkInEntity.setId(checkIn.getId());
            checkInEntity.setLastUpdateAt(ZonedDateTime.now(ZoneId.of("UTC")));
        }
        return checkInEntity;
    }

}
