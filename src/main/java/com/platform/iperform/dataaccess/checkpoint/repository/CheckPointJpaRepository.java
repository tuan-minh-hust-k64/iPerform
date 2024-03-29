package com.platform.iperform.dataaccess.checkpoint.repository;

import com.platform.iperform.common.valueobject.CategoryCheckpoint;
import com.platform.iperform.dataaccess.checkpoint.entity.CheckPointEntity;
import org.hibernate.query.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CheckPointJpaRepository extends JpaRepository<CheckPointEntity, UUID> {
    List<CheckPointEntity> findByUserIdOrderByCreatedAtDesc(UUID userId);
    Optional<CheckPointEntity> findByIdAndUserId(UUID id, UUID userId);
    Optional<CheckPointEntity> findByUserIdAndTitle(UUID userId, String title);
    @Query("Select distinct checkpoint from CheckPointEntity as checkpoint" +
            " where checkpoint.userId  =:userId" +
            " and (cast(:category as string) is null or checkpoint.category = :category )" +
            " and (cast(:title as string) is null or checkpoint.title = :title )")
    Optional<CheckPointEntity> findByTitleAndUserIdAndCategory(String title, UUID userId, CategoryCheckpoint category);
}
