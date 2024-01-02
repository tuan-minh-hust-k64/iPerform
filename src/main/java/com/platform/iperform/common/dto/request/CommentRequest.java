package com.platform.iperform.common.dto.request;

import com.platform.iperform.model.Comment;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class CommentRequest {
    private final UUID parentId;
    private final Comment comment;
    private final List<Comment> comments;
}
