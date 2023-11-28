package com.platform.iperform.service;

import com.platform.iperform.common.dto.EksRequest;
import com.platform.iperform.common.dto.EksResponse;
import com.platform.iperform.dataaccess.comment.adapter.CommentRepositoryImpl;
import com.platform.iperform.dataaccess.eks.adapter.EksRepositoryImpl;
import com.platform.iperform.model.Eks;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class EksService {
    private final EksRepositoryImpl eksRepository;
    private final CommentRepositoryImpl commentRepository;

    public EksService(EksRepositoryImpl eksRepository, CommentRepositoryImpl commentRepository) {
        this.eksRepository = eksRepository;
        this.commentRepository = commentRepository;
    }
    @Transactional
    public EksResponse createEks(List<Eks> eks) {
        List<Eks> result = eksRepository.save(eks);
        return EksResponse.builder()
                .eks(result)
                .build();
    }
    public EksResponse updateEks(EksRequest eksRequest) {

    }
    @Transactional(readOnly = true)
    public EksResponse getEksByUserId(UUID userId, String timePeriod) {
        Optional<List<Eks>> result = eksRepository.getEksByUserIdAndFilters(userId, timePeriod);
        return EksResponse.builder()
                .eks(result.orElse(List.of()))
                .build();
    }
}
