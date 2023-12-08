package com.platform.iperform.service;

import com.platform.iperform.common.dto.request.CheckInRequest;
import com.platform.iperform.common.dto.response.CheckInResponse;
import com.platform.iperform.common.exception.NotFoundException;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.dataaccess.eks.adapter.CheckInRepositoryImpl;
import com.platform.iperform.dataaccess.eks.entity.CheckInEntity;
import com.platform.iperform.dataaccess.eks.mapper.EksDataAccessMapper;
import com.platform.iperform.model.CheckIn;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

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
    public CheckInResponse updateCheckIn(CheckInRequest checkInRequest) {
        CheckInEntity checkInEntity = checkInRepository.findById(checkInRequest.getCheckIns().get(0).getId())
                .orElseThrow(() -> new NotFoundException("Not Found CheckIn with id: " + checkInRequest.getCheckIns().get(0).getId()));
        checkInEntity.setLastUpdateAt(ZonedDateTime.now(ZoneId.of("UTC")));
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

