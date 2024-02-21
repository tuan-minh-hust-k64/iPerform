package com.platform.iperform.dataaccess.eks.repository;

import com.platform.iperform.common.valueobject.Category;
import com.platform.iperform.common.valueobject.EksStatus;
import com.platform.iperform.dataaccess.eks.entity.EksEntity;
import com.platform.iperform.model.Eks;
import jakarta.persistence.NamedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface EksJpaRepository extends JpaRepository<EksEntity, UUID> {
    Optional<List<EksEntity>> findByUserIdAndCategoryOrUserIdAndTimePeriod(UUID userId, Category category, UUID userId2, String timePeriod);

    Optional<EksEntity> findByIdAndUserId(UUID id, UUID userId);

    @Query("Select distinct eks from EksEntity as eks" +
            " where eks.userId = :userId " +
            "and (cast(:category as string) is null or eks.category = :category) " +
            "and (cast(:timePeriod as string) is null or eks.timePeriod = :timePeriod)")
    Optional<List<EksEntity>> findByUserIdCategoryAndTimePeriod(UUID userId, Category category, String timePeriod);
}
