package com.platform.iperform.dataaccess.checkpoint.repository;

import com.platform.iperform.dataaccess.checkpoint.entity.CollaborationFeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CollaborationFeedbackJpaRepository extends JpaRepository<CollaborationFeedbackEntity, UUID> {
    List<CollaborationFeedbackEntity> findByReviewerId(UUID reviewerId);
}
