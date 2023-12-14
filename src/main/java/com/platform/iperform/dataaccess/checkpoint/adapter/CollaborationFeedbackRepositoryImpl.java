package com.platform.iperform.dataaccess.checkpoint.adapter;

import com.platform.iperform.dataaccess.checkpoint.entity.CollaborationFeedbackEntity;
import com.platform.iperform.dataaccess.checkpoint.repository.CollaborationFeedbackJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
    public List<CollaborationFeedbackEntity> getCollaborationByReviewerId(UUID reviewerId) {
        return collaborationFeedbackJpaRepository.findByReviewerId(reviewerId);
    }
    public Optional<CollaborationFeedbackEntity> findById(UUID id) {
        return collaborationFeedbackJpaRepository.findById(id);
    }
}
