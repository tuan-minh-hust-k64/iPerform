package com.platform.iperform.dataaccess.eks.repository;

import com.platform.iperform.dataaccess.eks.entity.KeyStepEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface KeyStepJpaRepository extends JpaRepository<KeyStepEntity, UUID> {

}
