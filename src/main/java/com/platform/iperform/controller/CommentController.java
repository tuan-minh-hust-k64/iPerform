package com.platform.iperform.controller;

import com.platform.iperform.common.dto.request.CommentRequest;
import com.platform.iperform.common.dto.response.CommentResponse;
import com.platform.iperform.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping(value = "/api/comment")


public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    @GetMapping
    public ResponseEntity<CommentResponse> getCommentByParentId(@RequestParam UUID parentId) {
        CommentResponse result = commentService.getCommentByParentId(CommentRequest.builder()
                        .parentId(parentId)
                .build());
        return ResponseEntity.ok(result);
    }
    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest commentRequest) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        commentRequest.getComment().setUserId(UUID.fromString(userId));
        CommentResponse result = commentService.createComment(commentRequest);
        return ResponseEntity.ok(result);
    }
    @PostMapping(value = "/create-many")
    public ResponseEntity<CommentResponse> createFeedback(@RequestBody CommentRequest commentRequest) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        commentRequest.getComments().forEach(item -> {
            item.setUserId(UUID.fromString(userId));
        });
        CommentResponse result = commentService.createFeedback(commentRequest);
        return ResponseEntity.ok(result);
    }
    @PutMapping
    public ResponseEntity<CommentResponse> updateComment(@RequestBody CommentRequest commentRequest) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        CommentResponse result = commentService.updateCommentByUserId(commentRequest, UUID.fromString(userId));
        return ResponseEntity.ok(result);
    }
}
