package com.platform.iperform.dataaccess.eks.adapter;

import com.platform.iperform.dataaccess.eks.entity.CheckInEntity;
import com.platform.iperform.dataaccess.eks.entity.KeyStepEntity;
import com.platform.iperform.dataaccess.eks.mapper.EksDataAccessMapper;
import com.platform.iperform.dataaccess.eks.repository.CheckInJpaRepository;
import com.platform.iperform.model.CheckIn;
import com.platform.iperform.model.KeyStep;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CheckInRepositoryImpl {
    private final EksDataAccessMapper eksDataAccessMapper;
    private final CheckInJpaRepository checkInJpaRepository;

    public CheckInRepositoryImpl(EksDataAccessMapper eksDataAccessMapper, CheckInJpaRepository checkInJpaRepository) {
        this.eksDataAccessMapper = eksDataAccessMapper;
        this.checkInJpaRepository = checkInJpaRepository;
    }
    public List<CheckIn> saveAll(List<CheckIn> checkIns) {
        List<CheckInEntity> result = checkInJpaRepository.saveAll(checkIns.stream().map(eksDataAccessMapper::checkInToCheckInEntity).toList());
        return eksDataAccessMapper.checkInEntitiesToCheckIn(result);
    }
    public CheckInEntity save(CheckInEntity checkInEntity) {
        return checkInJpaRepository.save(checkInEntity);
    }
    public Optional<CheckInEntity> findById(UUID id) {
        return checkInJpaRepository.findById(id);
    }
}
