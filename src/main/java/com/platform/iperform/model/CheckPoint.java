package com.platform.iperform.model;

import com.platform.iperform.common.valueobject.CheckPointStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class CheckPoint {
    private final UUID id;
    private final UUID userId;
    private final ZonedDateTime createdAt;
    private String title;
    private CheckPointStatus status;
    private ZonedDateTime lastUpdateAt;

    private final List<Comment> comments;
    private final List<CheckPointItem> checkPointItems;

    @Override
    public String toString() {
        return "CheckPoint{" +
                "id=" + id +
                ", userId=" + userId +
                ", createdAt=" + createdAt +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckPoint that = (CheckPoint) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId);
    }
}
