package com.platform.iperform.controller;

import com.platform.iperform.common.dto.request.CollaborationFeedbackRequest;
import com.platform.iperform.common.dto.response.CollaborationFeedbackResponse;
import com.platform.iperform.common.exception.AuthenticateException;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.service.CollaborationFeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@CrossOrigin(origins = {"http://localhost:3000", "https://iperform.ikameglobal.com"}, allowCredentials = "true")

@RequestMapping(value = "/api/collaboration-feedback")
public class CollaborationFeedbackController {
    private final CollaborationFeedbackService collaborationFeedbackService;
    private final FunctionHelper functionHelper;

    public CollaborationFeedbackController(CollaborationFeedbackService collaborationFeedbackService,
                                           FunctionHelper functionHelper) {
        this.collaborationFeedbackService = collaborationFeedbackService;
        this.functionHelper = functionHelper;
    }
    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<CollaborationFeedbackResponse> createCollaborationFeedback(@RequestBody CollaborationFeedbackRequest collaborationFeedbackRequest) {
        CollaborationFeedbackResponse result = collaborationFeedbackService.saveAll(collaborationFeedbackRequest.getCollaborationFeedbacks());
        return ResponseEntity.ok(result);
    }
    @GetMapping
    public ResponseEntity<CollaborationFeedbackResponse> getCollaborationFeedbackByReviewerId(@RequestParam UUID reviewerId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if(functionHelper.checkPermissionHrm(UUID.fromString(userId), reviewerId)) {
            CollaborationFeedbackResponse result = collaborationFeedbackService.getCollaborationByReviewerId(reviewerId);
            return ResponseEntity.ok(result);
        } else {
            throw new AuthenticateException("You are not permission!");
        }

    }
    @PutMapping
    public ResponseEntity<CollaborationFeedbackResponse> updateCollaborationFeedback(@RequestBody CollaborationFeedbackRequest collaborationFeedbackRequest) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if(functionHelper.checkPermissionHrm(UUID.fromString(userId), collaborationFeedbackRequest.getCollaborationFeedback().getReviewerId())) {
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
        if(functionHelper.checkPermissionHrm(UUID.fromString(userId), result.getData().getReviewerId())) {
            return ResponseEntity.ok(result);
        } else {
            throw new AuthenticateException("You are not permission!");
        }
    }
}
