package com.platform.iperform.common.dto;

import com.platform.iperform.model.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Setter
@Getter
public class CommentResponse {
    private final List<Comment> comment;
    private String message;
}
