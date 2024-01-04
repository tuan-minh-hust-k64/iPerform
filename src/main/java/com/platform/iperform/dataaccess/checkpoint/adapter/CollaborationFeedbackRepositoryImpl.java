package com.platform.iperform.dataaccess.checkpoint.adapter;

import com.platform.iperform.common.valueobject.FeedbackStatus;
import com.platform.iperform.dataaccess.checkpoint.entity.CollaborationFeedbackEntity;
import com.platform.iperform.dataaccess.checkpoint.repository.CollaborationFeedbackJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class CollaborationFeedbackRepositoryImpl {
    private final CollaborationFeedbackJpaRepository collaborationFeedbackJpaRepository;

    public CollaborationFeedbackRepositoryImpl(CollaborationFeedbackJpaRepository collaborationFeedbackJpaRepository) {
        this.collaborationFeedbackJpaRepository = collaborationFeedbackJpaRepository;
    }
    public CollaborationFeedbackEntity save(CollaborationFeedbackEntity collaborationFeedbackEntity) {
        return collaborationFeedbackJpaRepository.save(collaborationFeedbackEntity);
    }
    public List<CollaborationFeedbackEntity> saveAll(List<CollaborationFeedbackEntity> collaborationFeedbackEntityList) {
        return collaborationFeedbackJpaRepository.saveAll(collaborationFeedbackEntityList);
    }
    public List<CollaborationFeedbackEntity> getCollaborationByReviewerIdAndTimePeriod(UUID reviewerId, String timePeriod, FeedbackStatus... feedbackStatuses) {
        return collaborationFeedbackJpaRepository.findByReviewerIdAndTimePeriodAndStatusIn(reviewerId, timePeriod, feedbackStatuses);
    }
    public Optional<CollaborationFeedbackEntity> findById(UUID id) {
        return collaborationFeedbackJpaRepository.findById(id);
    }

    public List<CollaborationFeedbackEntity> getCollaborationByTargetIdAndTimePeriod(UUID targetId, String timePeriod, FeedbackStatus... feedbackStatuses) {
        return collaborationFeedbackJpaRepository.findByTargetIdAndTimePeriodAndStatusIn(targetId, timePeriod, feedbackStatuses);
    }
}
