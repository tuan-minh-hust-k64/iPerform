package com.platform.iperform.service;

import com.platform.iperform.common.dto.request.CollaborationFeedbackRequest;
import com.platform.iperform.common.dto.response.CollaborationFeedbackResponse;
import com.platform.iperform.common.exception.NotFoundException;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.common.valueobject.FeedbackStatus;
import com.platform.iperform.dataaccess.checkpoint.adapter.CollaborationFeedbackRepositoryImpl;
import com.platform.iperform.dataaccess.checkpoint.entity.CollaborationFeedbackEntity;
import com.platform.iperform.dataaccess.checkpoint.mapper.CheckPointDataAccessMapper;
import com.platform.iperform.model.CollaborationFeedback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class CollaborationFeedbackService {
    private final CollaborationFeedbackRepositoryImpl collaborationFeedbackRepository;
    private final CheckPointDataAccessMapper checkPointDataAccessMapper;
    private final FunctionHelper functionHelper;
    public CollaborationFeedbackService(CollaborationFeedbackRepositoryImpl collaborationFeedbackRepository,
                                        CheckPointDataAccessMapper checkPointDataAccessMapper, FunctionHelper functionHelper) {
        this.collaborationFeedbackRepository = collaborationFeedbackRepository;
        this.checkPointDataAccessMapper = checkPointDataAccessMapper;
        this.functionHelper = functionHelper;
    }
    @Transactional
    public CollaborationFeedbackResponse saveAll(List<CollaborationFeedback> collaborationFeedbacks) {
        List<CollaborationFeedback> result = collaborationFeedbackRepository.saveAll(
                collaborationFeedbacks.stream().map(
                        checkPointDataAccessMapper::collaborationFeedbackToCollaborationFeedbackEntity
                ).toList()
        ).stream().map(checkPointDataAccessMapper::collaborationFeedbackEntityToCollaborationFeedback).toList();
        return CollaborationFeedbackResponse.builder()
                .collaborationFeedbacks(result)
                .build();
    }
    @Transactional(readOnly = true)
    public CollaborationFeedbackResponse getCollaborationByReviewerIdAndTimePeriod(UUID reviewerId, String timePeriod, FeedbackStatus... feedbackStatuses){
        List<CollaborationFeedback> result = collaborationFeedbackRepository.getCollaborationByReviewerIdAndTimePeriod(reviewerId, timePeriod, feedbackStatuses)
                .stream().map(checkPointDataAccessMapper::collaborationFeedbackEntityToCollaborationFeedback).toList();
        return CollaborationFeedbackResponse.builder()
                .collaborationFeedbacks(result)
                .build();
    }
    @Transactional
    public CollaborationFeedbackResponse updateCollaborationFeedbackById(CollaborationFeedbackRequest collaborationFeedbackRequest) {
        CollaborationFeedbackEntity collaborationFeedbackEntity = collaborationFeedbackRepository.findById(collaborationFeedbackRequest.getCollaborationFeedback().getId())
                .orElseThrow(() -> new NotFoundException("Not found collaboration feedback with id: " + collaborationFeedbackRequest.getCollaborationFeedback().getId()));
        collaborationFeedbackEntity.setLastUpdateAt(ZonedDateTime.now(ZoneId.of("UTC")));
        collaborationFeedbackEntity.setStatus(FeedbackStatus.COMPLETED);
        BeanUtils.copyProperties(
                collaborationFeedbackRequest.getCollaborationFeedback(),
                collaborationFeedbackEntity,
                functionHelper.getNullPropertyNames(collaborationFeedbackRequest.getCollaborationFeedback())
        );
        CollaborationFeedbackEntity result = collaborationFeedbackRepository.save(collaborationFeedbackEntity);
        return CollaborationFeedbackResponse.builder()
                .data(checkPointDataAccessMapper.collaborationFeedbackEntityToCollaborationFeedback(result))
                .build();
    }
    @Transactional
    public CollaborationFeedbackResponse deleteCollaborationFeedbackById(CollaborationFeedbackRequest collaborationFeedbackRequest) {
        CollaborationFeedbackEntity collaborationFeedbackEntity = collaborationFeedbackRepository.findById(collaborationFeedbackRequest.getId())
                .orElseThrow(() -> new NotFoundException("Not found collaboration feedback with id: " + collaborationFeedbackRequest.getCollaborationFeedback().getId()));
        collaborationFeedbackEntity.setLastUpdateAt(ZonedDateTime.now(ZoneId.of("UTC")));
        collaborationFeedbackEntity.setStatus(FeedbackStatus.DELETED);
        CollaborationFeedbackEntity result = collaborationFeedbackRepository.save(collaborationFeedbackEntity);
        return CollaborationFeedbackResponse.builder()
                .data(checkPointDataAccessMapper.collaborationFeedbackEntityToCollaborationFeedback(result))
                .build();
    }
    @Transactional(readOnly = true)
    public CollaborationFeedbackResponse findById(UUID id) {
        CollaborationFeedbackEntity result = collaborationFeedbackRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found collaboration feedback with id: " + id));
        return CollaborationFeedbackResponse.builder()
                .data(checkPointDataAccessMapper.collaborationFeedbackEntityToCollaborationFeedback(result))
                .build();
    }

    public CollaborationFeedbackResponse getCollaborationByTargetIdAndTimePeriod(UUID targetId, String timePeriod, FeedbackStatus... feedbackStatuses) {
        List<CollaborationFeedback> result = collaborationFeedbackRepository.getCollaborationByTargetIdAndTimePeriod(targetId, timePeriod, feedbackStatuses)
                .stream().map(checkPointDataAccessMapper::collaborationFeedbackEntityToCollaborationFeedback).toList();
        return CollaborationFeedbackResponse.builder()
                .collaborationFeedbacks(result)
                .build();
    }
}
