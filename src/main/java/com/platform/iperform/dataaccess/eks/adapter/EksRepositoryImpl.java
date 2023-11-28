package com.platform.iperform.dataaccess.eks.adapter;

import com.platform.iperform.common.dto.EksRequest;
import com.platform.iperform.common.valueobject.EksStatus;
import com.platform.iperform.dataaccess.eks.entity.EksEntity;
import com.platform.iperform.dataaccess.eks.exception.EksNotFoundException;
import com.platform.iperform.dataaccess.eks.mapper.EksDataAccessMapper;
import com.platform.iperform.dataaccess.eks.repository.EksJpaRepository;
import com.platform.iperform.model.Eks;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@Component
public class EksRepositoryImpl {
    public final EksJpaRepository eksJpaRepository;
    public final EksDataAccessMapper eksDataAccessMapper;

    public EksRepositoryImpl(EksJpaRepository eksJpaRepository, EksDataAccessMapper eksDataAccessMapper) {
        this.eksJpaRepository = eksJpaRepository;
        this.eksDataAccessMapper = eksDataAccessMapper;
    }

    public Optional<List<Eks>> getEksByUserIdAndFilters(UUID userId, String timePeriod) {
        return Optional.of(eksJpaRepository.findByUserIdAndTimePeriod(userId, timePeriod)
                .orElseThrow(() -> new EksNotFoundException("Not found eks with userId " + userId + ", timePeriod: " + timePeriod))
                .stream()
                .map(eksDataAccessMapper::eksEntityToEks).collect(Collectors.toList()));
    }
    public List<Eks> save(List<Eks> eks) {
        List<EksEntity> result = eksJpaRepository.saveAll(eks.stream().map(eksDataAccessMapper::eksToEksEntity).toList());
        return result.stream().map(eksDataAccessMapper::eksEntityToEks).toList();
    }
    public Eks updateEks(EksRequest eksRequest) {

    }

}
