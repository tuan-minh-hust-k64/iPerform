package com.platform.iperform.controller;

import com.platform.iperform.common.dto.CommentRequest;
import com.platform.iperform.common.dto.CommentResponse;
import com.platform.iperform.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    @PostMapping(value = "/get-comment-by-parent-id")
    public ResponseEntity<CommentResponse> getCommentByParentId(@RequestBody CommentRequest commentRequest) {
        CommentResponse result = commentService.getCommentByParentId(commentRequest);
        return ResponseEntity.ok(result);
    }
    @PostMapping(value = "/create")
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest commentRequest) {
        CommentResponse result = commentService.createComment(commentRequest);
        return ResponseEntity.ok(result);
    }
    @PostMapping(value = "/update")
    public ResponseEntity<CommentResponse> updateComment(@RequestBody CommentRequest commentRequest) {
        CommentResponse result = commentService.updateComment(commentRequest);
        return ResponseEntity.ok(result);
    }
}
