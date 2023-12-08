package com.platform.iperform.dataaccess.eks.repository;

import com.platform.iperform.common.valueobject.EksStatus;
import com.platform.iperform.dataaccess.eks.entity.EksEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface EksJpaRepository extends JpaRepository<EksEntity, UUID> {
    Optional<List<EksEntity>> findByUserIdAndTimePeriod(UUID userId, String timePeriod);
    Optional<EksEntity> findByIdAndUserId(UUID id, UUID userId);
}
