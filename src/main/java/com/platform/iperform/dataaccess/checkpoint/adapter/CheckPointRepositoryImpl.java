package com.platform.iperform.dataaccess.checkpoint.adapter;

import com.platform.iperform.dataaccess.checkpoint.mapper.CheckPointDataAccessMapper;
import com.platform.iperform.dataaccess.checkpoint.repository.CheckPointJpaRepository;
import com.platform.iperform.model.CheckPoint;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CheckPointRepositoryImpl {
    private final CheckPointJpaRepository checkPointJpaRepository;
    private final CheckPointDataAccessMapper checkPointDataAccessMapper;

    public CheckPointRepositoryImpl(CheckPointJpaRepository checkPointJpaRepository, CheckPointDataAccessMapper checkPointDataAccessMapper) {
        this.checkPointJpaRepository = checkPointJpaRepository;
        this.checkPointDataAccessMapper = checkPointDataAccessMapper;
    }

    public List<CheckPoint> getCheckPointByUserId(UUID userId, Pageable pageable) {
        return checkPointJpaRepository.findByUserIdOrderByCreatedAtDesc(
                userId, pageable
        ).stream().map(checkPointDataAccessMapper::checkPointEntityToCheckPoint).toList();
    }

    public CheckPoint save(CheckPoint checkPoint) {
        return checkPointDataAccessMapper.checkPointEntityToCheckPoint(
                checkPointJpaRepository.save(
                        checkPointDataAccessMapper.checkPointToCheckPointEntity(checkPoint)
                )
        );
    }
}
