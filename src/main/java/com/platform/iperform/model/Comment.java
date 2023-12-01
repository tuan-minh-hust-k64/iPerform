package com.platform.iperform.model;

import com.platform.iperform.common.valueobject.CommentStatus;
import com.platform.iperform.common.valueobject.CommentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class Comment {
    private final UUID id;
    private final UUID userId;
    private final UUID parentId;
    private final ZonedDateTime createdAt;
    private CommentType type;
    private String content;
    private ZonedDateTime lastUpdateAt;
    private UUID questionId;
    private CommentStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", userId=" + userId +
                ", checkPointId=" + parentId +
                ", createdAt=" + createdAt +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", lastUpdateAt=" + lastUpdateAt +
                '}';
    }
}
