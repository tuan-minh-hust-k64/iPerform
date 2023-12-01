package com.platform.iperform.dataaccess.comment.repository;

import com.platform.iperform.common.valueobject.QuestionStatus;
import com.platform.iperform.dataaccess.comment.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestionJpaRepository extends JpaRepository<QuestionEntity, UUID> {
    List<QuestionEntity> findByStatus(QuestionStatus status);
}
