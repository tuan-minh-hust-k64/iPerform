package com.platform.iperform.service;

import com.platform.iperform.common.dto.CheckPointRequest;
import com.platform.iperform.common.dto.CheckPointResponse;
import com.platform.iperform.common.dto.EksRequest;
import com.platform.iperform.common.dto.EksResponse;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.common.valueobject.CheckPointStatus;
import com.platform.iperform.dataaccess.checkpoint.adapter.CheckPointRepositoryImpl;
import com.platform.iperform.dataaccess.checkpoint.entity.CheckPointEntity;
import com.platform.iperform.dataaccess.checkpoint.mapper.CheckPointDataAccessMapper;
import com.platform.iperform.dataaccess.eks.entity.EksEntity;
import com.platform.iperform.dataaccess.eks.exception.EksNotFoundException;
import com.platform.iperform.model.CheckPoint;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class CheckPointService {
    private final CheckPointRepositoryImpl checkPointRepository;
    private final CheckPointDataAccessMapper checkPointDataAccessMapper;
    private final FunctionHelper functionHelper;

    public CheckPointService(CheckPointRepositoryImpl checkPointRepository, CheckPointDataAccessMapper checkPointDataAccessMapper, FunctionHelper functionHelper) {
        this.checkPointRepository = checkPointRepository;
        this.checkPointDataAccessMapper = checkPointDataAccessMapper;
        this.functionHelper = functionHelper;
    }
    @Transactional(readOnly = true)
    public CheckPointResponse getCheckPointByUserId(CheckPointRequest checkPointRequest) {
        List<CheckPoint> result = checkPointRepository.getCheckPointByUserId(checkPointRequest.getUserId());
        return CheckPointResponse.builder()
                .checkPoint(result)
                .build();
    }
    @Transactional
    public CheckPointResponse createCheckPoint(CheckPointRequest checkPointRequest) {
        checkPointRequest.getCheckPoint().setStatus(CheckPointStatus.INIT);
        CheckPoint result = checkPointRepository.save(checkPointRequest.getCheckPoint());
        return CheckPointResponse.builder()
                .checkPoint(List.of(result))
                .build();
    }
    @Transactional
    public CheckPointResponse updateCheckPoint(CheckPointRequest checkPointRequest) {
        CheckPointEntity checkPointEntity = checkPointRepository.findById(checkPointRequest.getCheckPoint().getId())
                .orElseThrow(() -> new EksNotFoundException("Not Found CheckPoint with id: " + checkPointRequest.getCheckPoint().getId()));
        checkPointEntity.setLastUpdateAt(ZonedDateTime.now(ZoneId.of("UTC")));
        BeanUtils.copyProperties(
                checkPointRequest.getCheckPoint(),
                checkPointEntity,
                functionHelper.getNullPropertyNames(checkPointRequest.getCheckPoint())
        );
        CheckPoint result = checkPointRepository.save(checkPointDataAccessMapper.checkPointEntityToCheckPoint(checkPointEntity));
        return CheckPointResponse.builder()
                .checkPoint(List.of(result))
                .build();
    }
}
