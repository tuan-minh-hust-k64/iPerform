package com.platform.iperform.common.dto;

import com.platform.iperform.model.Comment;
import lombok.Builder;

import java.util.List;

@Builder
public class CommentResponse {
    private final List<Comment> comment;
    private String message;
}
