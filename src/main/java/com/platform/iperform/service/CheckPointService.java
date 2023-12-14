package com.platform.iperform.service;

import com.platform.iperform.common.dto.request.CheckPointRequest;
import com.platform.iperform.common.dto.response.CheckPointResponse;
import com.platform.iperform.common.exception.NotFoundException;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.dataaccess.checkpoint.adapter.CheckPointItemRepositoryImpl;
import com.platform.iperform.dataaccess.checkpoint.adapter.CheckPointRepositoryImpl;
import com.platform.iperform.dataaccess.checkpoint.entity.CheckPointEntity;
import com.platform.iperform.dataaccess.checkpoint.mapper.CheckPointDataAccessMapper;
import com.platform.iperform.model.CheckPoint;
import com.platform.iperform.model.CheckPointItem;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CheckPointService {
    private final CheckPointRepositoryImpl checkPointRepository;
    private final CheckPointItemRepositoryImpl checkPointItemRepository;
    private final CheckPointDataAccessMapper checkPointDataAccessMapper;
    private final FunctionHelper functionHelper;

    public CheckPointService(CheckPointRepositoryImpl checkPointRepository,
                             CheckPointItemRepositoryImpl checkPointItemRepository,
                             CheckPointDataAccessMapper checkPointDataAccessMapper,
                             FunctionHelper functionHelper) {
        this.checkPointRepository = checkPointRepository;
        this.checkPointItemRepository = checkPointItemRepository;
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
        CheckPoint result = checkPointDataAccessMapper.checkPointEntityToCheckPoint(
                checkPointRepository.save(checkPointDataAccessMapper.checkPointToCheckPointEntity(checkPointRequest.getCheckPoint()))
        );
        return CheckPointResponse.builder()
                .data(result)
                .build();
    }
    @Transactional
    public CheckPointResponse updateCheckPointByUserId(CheckPointRequest checkPointRequest, UUID userId) {
        CheckPointEntity checkPointEntity = checkPointRepository.findByIdAndUserId(checkPointRequest.getCheckPoint().getId(), userId)
                .orElseThrow(() -> new NotFoundException("Not Found CheckPoint with id: " + checkPointRequest.getCheckPoint().getId()));
        checkPointEntity.setLastUpdateAt(ZonedDateTime.now(ZoneId.of("UTC")));
        List<CheckPointItem> checkPointItemEntities = checkPointRequest.getCheckPoint().getCheckPointItems();
        checkPointItemRepository.saveAll(checkPointItemEntities);
        BeanUtils.copyProperties(
                checkPointRequest.getCheckPoint(),
                checkPointEntity,
                functionHelper.getNullPropertyNames(checkPointRequest.getCheckPoint())
        );
        CheckPointEntity result = checkPointRepository.save(checkPointEntity);
        return CheckPointResponse.builder()
                .data(checkPointDataAccessMapper.checkPointEntityToCheckPoint(result))
                .build();
    }

    @Transactional(readOnly = true)
    public CheckPointResponse findByIdAndUserId(UUID id, UUID userId) {
        CheckPointEntity result = checkPointRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("CheckPoint not found with id: " + id));
        return CheckPointResponse.builder()
                .data(checkPointDataAccessMapper.checkPointEntityToCheckPoint(result))
                .build();
    }
    @Transactional(readOnly = true)
    public CheckPointResponse findById(UUID id) {
        CheckPointEntity result = checkPointRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("CheckPoint not found with id: " + id));
        return CheckPointResponse.builder()
                .data(checkPointDataAccessMapper.checkPointEntityToCheckPoint(result))
                .build();
    }
}
