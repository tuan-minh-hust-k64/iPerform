package com.platform.iperform.dataaccess.eks.adapter;

import com.platform.iperform.dataaccess.eks.entity.KeyStepEntity;
import com.platform.iperform.dataaccess.eks.mapper.EksDataAccessMapper;
import com.platform.iperform.dataaccess.eks.repository.KeyStepJpaRepository;
import com.platform.iperform.model.KeyStep;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class KeyStepRepositoryImpl {
    private final KeyStepJpaRepository keyStepJpaRepository;
    private final EksDataAccessMapper eksDataAccessMapper;

    public KeyStepRepositoryImpl(KeyStepJpaRepository keyStepJpaRepository, EksDataAccessMapper eksDataAccessMapper) {
        this.keyStepJpaRepository = keyStepJpaRepository;
        this.eksDataAccessMapper = eksDataAccessMapper;
    }

    public List<KeyStep> saveAll(List<KeyStep> keySteps) {
        List<KeyStepEntity> result = keyStepJpaRepository.saveAll(eksDataAccessMapper.keyStepsToKeyStepEntities(keySteps));
        return eksDataAccessMapper.keyStepEntitiesToKeySteps(result);
    }
    public KeyStepEntity save(KeyStepEntity keyStepEntity) {
        return keyStepJpaRepository.save(keyStepEntity);
    }
    public Optional<KeyStepEntity> findById(UUID id) {
        return keyStepJpaRepository.findById(id);
    }
}
