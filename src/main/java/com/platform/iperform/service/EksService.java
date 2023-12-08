package com.platform.iperform.service;

import com.platform.iperform.common.dto.request.EksRequest;
import com.platform.iperform.common.dto.response.EksResponse;
import com.platform.iperform.common.exception.NotFoundException;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.dataaccess.comment.adapter.CommentRepositoryImpl;
import com.platform.iperform.dataaccess.eks.adapter.CheckInRepositoryImpl;
import com.platform.iperform.dataaccess.eks.adapter.EksRepositoryImpl;
import com.platform.iperform.dataaccess.eks.adapter.KeyStepRepositoryImpl;
import com.platform.iperform.dataaccess.eks.entity.EksEntity;
import com.platform.iperform.dataaccess.eks.mapper.EksDataAccessMapper;
import com.platform.iperform.model.CheckIn;
import com.platform.iperform.model.Eks;
import com.platform.iperform.model.KeyStep;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class EksService {
    private final EksRepositoryImpl eksRepository;
    private final EksDataAccessMapper eksDataAccessMapper;
    private final FunctionHelper functionHelper;
    private final KeyStepRepositoryImpl keyStepRepository;
    private final CheckInRepositoryImpl checkInRepository;


    public EksService(EksRepositoryImpl eksRepository,
                      EksDataAccessMapper eksDataAccessMapper, CommentRepositoryImpl commentRepository,
                      FunctionHelper functionHelper, KeyStepRepositoryImpl keyStepRepository, CheckInRepositoryImpl checkInRepository) {
        this.eksRepository = eksRepository;
        this.eksDataAccessMapper = eksDataAccessMapper;
        this.functionHelper = functionHelper;
        this.keyStepRepository = keyStepRepository;
        this.checkInRepository = checkInRepository;
    }
    @Transactional
    public EksResponse createEks(List<Eks> eks) {
        List<Eks> result = eksRepository.saveAll(eks);
        return EksResponse.builder()
                .eks(result)
                .build();
    }
    @Transactional
    public EksResponse updateEksByIdAndUserId(EksRequest eksRequest, UUID userId) {
        EksEntity eksEntity = eksRepository.findByIdAndUserId(eksRequest.getData().getId(), userId)
                .orElseThrow(() -> new NotFoundException("Not Found Eks with id: " + eksRequest.getData().getId()));
        eksEntity.setLastUpdateAt(ZonedDateTime.now(ZoneId.of("UTC")));
        List<KeyStep> keySteps = eksRequest.getData().getKeySteps();
        List<CheckIn> checkIns = eksRequest.getData().getCheckIns();
        checkInRepository.saveAll(checkIns);
        keyStepRepository.saveAll(keySteps);
        BeanUtils.copyProperties(
                eksRequest.getData(),
                eksEntity,
                functionHelper.getNullPropertyNames(eksRequest.getData())
        );
        EksEntity result = eksRepository.save(eksEntity);
        return EksResponse.builder()
                .eks(List.of(eksDataAccessMapper.eksEntityToEks(result)))
                .build();
    }



    @Transactional(readOnly = true)
    public EksResponse getEksByUserId(UUID userId, String timePeriod) {
        Optional<List<Eks>> result = eksRepository.getEksByUserIdAndFilters(userId, timePeriod);
        return EksResponse.builder()
                .eks(result.orElse(List.of()))
                .build();
    }

    @Transactional(readOnly = true)
    public EksResponse getEksById(UUID eksId) {
        EksEntity result = eksRepository.findById(eksId)
                .orElseThrow(() -> new NotFoundException("Not Found Eks with id: " + eksId));
        return EksResponse.builder()
                .data(eksDataAccessMapper.eksEntityToEks(result))
                .build();
    }

    @Transactional(readOnly = true)
    public EksResponse getEksByIdAndUserId(UUID id, UUID userId) {
        EksEntity result = eksRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("Not Found Eks with id: " + id));
        return EksResponse.builder()
                .data(eksDataAccessMapper.eksEntityToEks(result))
                .build();
    }
}
