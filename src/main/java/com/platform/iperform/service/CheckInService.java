package com.platform.iperform.service;

import com.platform.iperform.common.dto.CheckInRequest;
import com.platform.iperform.common.dto.CheckInResponse;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.dataaccess.eks.adapter.CheckInRepositoryImpl;
import com.platform.iperform.dataaccess.eks.entity.CheckInEntity;
import com.platform.iperform.dataaccess.eks.exception.CheckInNotFoundException;
import com.platform.iperform.dataaccess.eks.mapper.EksDataAccessMapper;
import com.platform.iperform.model.CheckIn;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
public class CheckInService {
    private final CheckInRepositoryImpl checkInRepository;
    private final EksDataAccessMapper eksDataAccessMapper;
    private final FunctionHelper functionHelper;

    public CheckInService(CheckInRepositoryImpl checkInRepository, EksDataAccessMapper eksDataAccessMapper, FunctionHelper functionHelper) {
        this.checkInRepository = checkInRepository;
        this.eksDataAccessMapper = eksDataAccessMapper;
        this.functionHelper = functionHelper;
    }
    @Transactional
    public CheckInResponse createCheckIn(CheckInRequest checkInRequest) {
        List<CheckIn> result = checkInRepository.saveAll(checkInRequest.getCheckIns());
        return CheckInResponse.builder()
                .checkIns(result)
                .build();
    }
    @Transactional
    public CheckInResponse updateKeyStep(CheckInRequest checkInRequest) {
        CheckInEntity checkInEntity = checkInRepository.findById(checkInRequest.getId())
                .orElseThrow(() -> new CheckInNotFoundException("Not Found CheckIn with id: " + checkInRequest.getId()));
        BeanUtils.copyProperties(
                checkInRequest.getCheckIns().get(0),
                checkInEntity,
                functionHelper.getNullPropertyNames(checkInRequest.getCheckIns().get(0))
        );
        CheckInEntity result = checkInRepository.save(checkInEntity);
        return CheckInResponse.builder()
                .checkIns(eksDataAccessMapper.checkInEntitiesToCheckIn(List.of(result)))
                .build();
    }
}

