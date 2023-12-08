package com.platform.iperform.controller;

import com.platform.iperform.common.dto.CheckInResponse;
import com.platform.iperform.common.dto.CommentRequest;
import com.platform.iperform.common.dto.CommentResponse;
import com.platform.iperform.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping(value = "/comment")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")


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
        CommentResponse result = commentService.createComment(commentRequest);
        return ResponseEntity.ok(result);
    }
    @PutMapping
    public ResponseEntity<CommentResponse> updateComment(@RequestBody CommentRequest commentRequest) {
        CommentResponse result = commentService.updateComment(commentRequest);
        return ResponseEntity.ok(result);
    }
}
