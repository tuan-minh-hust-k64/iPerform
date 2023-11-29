package com.platform.iperform.dataaccess.checkpoint.adapter;

import com.platform.iperform.dataaccess.checkpoint.entity.CheckPointItemEntity;
import com.platform.iperform.dataaccess.checkpoint.mapper.CheckPointDataAccessMapper;
import com.platform.iperform.dataaccess.checkpoint.repository.CheckPointItemJpaRepository;
import com.platform.iperform.model.CheckPointItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CheckPointItemRepositoryImpl {
    private final CheckPointDataAccessMapper checkPointDataAccessMapper;
    private final CheckPointItemJpaRepository checkPointItemJpaRepository;

    public CheckPointItemRepositoryImpl(CheckPointDataAccessMapper checkPointDataAccessMapper, CheckPointItemJpaRepository checkPointItemJpaRepository) {
        this.checkPointDataAccessMapper = checkPointDataAccessMapper;
        this.checkPointItemJpaRepository = checkPointItemJpaRepository;
    }
    public CheckPointItemEntity save(CheckPointItem checkPointItem) {
        return checkPointItemJpaRepository.save(
                checkPointDataAccessMapper.checkPointItemsToCheckPointItemEntities(List.of(checkPointItem)).get(0)
        );
    }
    public List<CheckPointItemEntity> saveAll(List<CheckPointItem> checkPointItems) {
        return checkPointItemJpaRepository.saveAll(
                checkPointDataAccessMapper.checkPointItemsToCheckPointItemEntities(checkPointItems)
        );
    }
    public Optional<CheckPointItemEntity> findById(UUID id) {
        return checkPointItemJpaRepository.findById(id);
    }
}
