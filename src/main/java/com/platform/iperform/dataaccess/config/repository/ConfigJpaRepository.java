package com.platform.iperform.dataaccess.config.repository;

import com.platform.iperform.dataaccess.config.entity.ConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface ConfigJpaRepository extends JpaRepository<ConfigEntity, UUID> {
}
