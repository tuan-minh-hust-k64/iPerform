package com.platform.iperform.dataaccess.eks.adapter;

import com.platform.iperform.dataaccess.eks.entity.EksEntity;
import com.platform.iperform.dataaccess.eks.exception.EksNotFoundException;
import com.platform.iperform.dataaccess.eks.mapper.EksDataAccessMapper;
import com.platform.iperform.dataaccess.eks.repository.EksJpaRepository;
import com.platform.iperform.dataaccess.eks.repository.KeyStepJpaRepository;
import com.platform.iperform.model.Eks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@Component
@Slf4j
public class EksRepositoryImpl {
    private final EksJpaRepository eksJpaRepository;
    private final EksDataAccessMapper eksDataAccessMapper;

    public EksRepositoryImpl(EksJpaRepository eksJpaRepository, EksDataAccessMapper eksDataAccessMapper, KeyStepJpaRepository keyStepJpaRepository) {
        this.eksJpaRepository = eksJpaRepository;
        this.eksDataAccessMapper = eksDataAccessMapper;
    }

    public Optional<List<Eks>> getEksByUserIdAndFilters(UUID userId, String timePeriod) {
        return Optional.of(eksJpaRepository.findByUserIdAndTimePeriod(userId, timePeriod)
                .orElseThrow(() -> new EksNotFoundException("Not found eks with userId " + userId + ", timePeriod: " + timePeriod))
                .stream()
                .map(eksDataAccessMapper::eksEntityToEks).collect(Collectors.toList()));
    }
    public List<Eks> saveAll(List<Eks> eks) {
        List<EksEntity> result = eksJpaRepository.saveAll(eks.stream().map(eksDataAccessMapper::eksToEksEntity).toList());
        log.info("EKS: " + result.get(0).toString());
        return result.stream().map(eksDataAccessMapper::eksEntityToEks).toList();
    }
    public EksEntity save(EksEntity eksEntity) {
        return eksJpaRepository.save(eksEntity);
    }
    public Optional<EksEntity> findById(UUID eId) {
        return eksJpaRepository.findById(eId);
    }

}
