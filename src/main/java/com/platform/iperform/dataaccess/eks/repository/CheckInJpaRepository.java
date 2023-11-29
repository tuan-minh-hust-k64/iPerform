package com.platform.iperform.dataaccess.eks.repository;

import com.platform.iperform.dataaccess.eks.entity.CheckInEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CheckInJpaRepository extends JpaRepository<CheckInEntity, UUID> {
}
