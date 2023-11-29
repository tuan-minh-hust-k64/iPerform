package com.platform.iperform.dataaccess.checkpoint.repository;

import com.platform.iperform.dataaccess.checkpoint.entity.CheckPointItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CheckPointItemJpaRepository extends JpaRepository<CheckPointItemEntity, UUID> {
}
