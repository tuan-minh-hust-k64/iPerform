package com.platform.iperform.service;

import com.platform.iperform.common.dto.KeyStepRequest;
import com.platform.iperform.common.dto.KeyStepResponse;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.dataaccess.eks.adapter.KeyStepRepositoryImpl;
import com.platform.iperform.dataaccess.eks.entity.KeyStepEntity;
import com.platform.iperform.dataaccess.eks.exception.EksNotFoundException;
import com.platform.iperform.dataaccess.eks.mapper.EksDataAccessMapper;
import com.platform.iperform.model.KeyStep;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
public class KeyStepService {
    private final KeyStepRepositoryImpl keyStepRepository;
    private final FunctionHelper functionHelper;
    private final EksDataAccessMapper eksDataAccessMapper;
    public KeyStepService(KeyStepRepositoryImpl keyStepRepository, FunctionHelper functionHelper, EksDataAccessMapper eksDataAccessMapper) {
        this.keyStepRepository = keyStepRepository;
        this.functionHelper = functionHelper;
        this.eksDataAccessMapper = eksDataAccessMapper;
    }
    @Transactional
    public KeyStepResponse createKeyStep(KeyStepRequest keyStepRequest) {
        List<KeyStep> result = keyStepRepository.saveAll(keyStepRequest.getKeySteps());
        return KeyStepResponse.builder()
                .keySteps(result)
                .build();
    }
    @Transactional
    public KeyStepResponse updateKeyStep(UUID id, KeyStepRequest keyStepRequest) {
        KeyStepEntity eksEntity = keyStepRepository.findById(id)
                .orElseThrow(() -> new EksNotFoundException("Not Found keyStep with id: " + id));
        BeanUtils.copyProperties(
                keyStepRequest.getKeySteps().get(0),
                eksEntity,
                functionHelper.getNullPropertyNames(keyStepRequest.getKeySteps().get(0))
        );
        KeyStepEntity result = keyStepRepository.save(eksEntity);
        return KeyStepResponse.builder()
                .keySteps(eksDataAccessMapper.keyStepEntitiesToKeySteps(List.of(result)))
                .build();
    }
}
