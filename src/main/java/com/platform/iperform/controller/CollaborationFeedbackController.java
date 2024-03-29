package com.platform.iperform.controller;

import com.platform.iperform.common.dto.request.CollaborationFeedbackRequest;
import com.platform.iperform.common.dto.response.CollaborationFeedbackResponse;
import com.platform.iperform.common.exception.AuthenticateException;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.common.valueobject.FeedbackStatus;
import com.platform.iperform.libs.hrms_provider.HrmsProvider;
import com.platform.iperform.libs.hrms_provider.HrmsV3;
import com.platform.iperform.service.CollaborationFeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@Slf4j
@RequestMapping(value = "/api/collaboration-feedback")
public class CollaborationFeedbackController {
    private final CollaborationFeedbackService collaborationFeedbackService;
    private final FunctionHelper functionHelper;
    private final HrmsProvider hrmsProvider;

    public CollaborationFeedbackController(
            CollaborationFeedbackService collaborationFeedbackService,
            FunctionHelper functionHelper,
            HrmsV3 hrmsV3
    ) {
        this.collaborationFeedbackService = collaborationFeedbackService;
        this.functionHelper = functionHelper;
        this.hrmsProvider = hrmsV3;
    }
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    public ResponseEntity<CollaborationFeedbackResponse> createCollaborationFeedback(@RequestBody CollaborationFeedbackRequest collaborationFeedbackRequest) {
        CollaborationFeedbackResponse result = collaborationFeedbackService.saveAll(collaborationFeedbackRequest.getCollaborationFeedbacks());
        return ResponseEntity.ok(result);
    }
    @PostMapping(value = "/delete-by-id")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    public ResponseEntity<CollaborationFeedbackResponse> deleteCollaborationFeedback(@RequestBody CollaborationFeedbackRequest collaborationFeedbackRequest) {
        CollaborationFeedbackResponse result = collaborationFeedbackService.deleteCollaborationFeedbackById(collaborationFeedbackRequest);
        return ResponseEntity.ok(result);
    }
    @GetMapping
    public ResponseEntity<CollaborationFeedbackResponse> getCollaborationFeedbackByReviewerId(@RequestParam UUID reviewerId, @RequestParam String timePeriod) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if(hrmsProvider.checkPermissionHrm(UUID.fromString(userId), reviewerId)) {
            CollaborationFeedbackResponse result = collaborationFeedbackService.getCollaborationByReviewerIdAndTimePeriod(reviewerId, timePeriod, FeedbackStatus.COMPLETED, FeedbackStatus.INIT);
            return ResponseEntity.ok(result);
        } else {
            throw new AuthenticateException("You are not permission!");
        }

    }

    @PostMapping(value = "/get-by-target-id")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    public ResponseEntity<CollaborationFeedbackResponse> getCollaborationFeedbackByTargetId(@RequestBody CollaborationFeedbackRequest collaborationFeedbackRequest) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if(hrmsProvider.checkPermissionHrm(UUID.fromString(userId), collaborationFeedbackRequest.getTargetId())) {
            CollaborationFeedbackResponse result = collaborationFeedbackService.getCollaborationByTargetIdAndTimePeriod(
                    collaborationFeedbackRequest.getTargetId(),
                    collaborationFeedbackRequest.getTimePeriod(),
                    FeedbackStatus.COMPLETED,
                    FeedbackStatus.INIT
                    );
            return ResponseEntity.ok(result);
        } else {
            throw new AuthenticateException("You are not permission!");
        }
    }
    @PutMapping
    public ResponseEntity<CollaborationFeedbackResponse> updateCollaborationFeedback(@RequestBody CollaborationFeedbackRequest collaborationFeedbackRequest) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if(hrmsProvider.checkPermissionHrm(UUID.fromString(userId), collaborationFeedbackRequest.getCollaborationFeedback().getReviewerId())) {
            CollaborationFeedbackResponse result = collaborationFeedbackService.updateCollaborationFeedbackById(collaborationFeedbackRequest);
            return ResponseEntity.ok(result);
        } else {
            throw new AuthenticateException("You are not permission!");
        }
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<CollaborationFeedbackResponse> getCollaborationFeedbackById(@PathVariable UUID id) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        CollaborationFeedbackResponse result = collaborationFeedbackService.findById(id);
        if(hrmsProvider.checkPermissionHrm(UUID.fromString(userId), result.getData().getReviewerId())
                || hrmsProvider.checkPermissionHrm(UUID.fromString(userId), result.getData().getTargetId())) {
            return ResponseEntity.ok(result);
        } else {
            throw new AuthenticateException("You are not permission!");
        }
    }
}
