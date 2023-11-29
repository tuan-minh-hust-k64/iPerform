package com.platform.iperform.service;

import com.platform.iperform.common.dto.EksRequest;
import com.platform.iperform.common.dto.EksResponse;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.dataaccess.comment.adapter.CommentRepositoryImpl;
import com.platform.iperform.dataaccess.eks.adapter.EksRepositoryImpl;
import com.platform.iperform.dataaccess.eks.entity.EksEntity;
import com.platform.iperform.dataaccess.eks.exception.EksNotFoundException;
import com.platform.iperform.dataaccess.eks.mapper.EksDataAccessMapper;
import com.platform.iperform.model.Eks;
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

    public EksService(EksRepositoryImpl eksRepository,
                      EksDataAccessMapper eksDataAccessMapper, CommentRepositoryImpl commentRepository,
                      FunctionHelper functionHelper) {
        this.eksRepository = eksRepository;
        this.eksDataAccessMapper = eksDataAccessMapper;
        this.functionHelper = functionHelper;
    }
    @Transactional
    public EksResponse createEks(List<Eks> eks) {
        List<Eks> result = eksRepository.saveAll(eks);
        return EksResponse.builder()
                .eks(result)
                .build();
    }
    @Transactional
    public EksResponse updateEks(EksRequest eksRequest) {
        EksEntity eksEntity = eksRepository.findById(eksRequest.getEks().get(0).getId())
                .orElseThrow(() -> new EksNotFoundException("Not Found Eks with id: " + eksRequest.getEks().get(0).getId()));
        eksEntity.setLastUpdateAt(ZonedDateTime.now(ZoneId.of("UTC")));
        BeanUtils.copyProperties(
                eksRequest.getEks().get(0),
                eksEntity,
                functionHelper.getNullPropertyNames(eksRequest.getEks().get(0))
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
}
