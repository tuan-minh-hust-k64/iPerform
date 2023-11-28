package com.platform.iperform.model;

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
public class CheckPointItem {
    private final UUID id;
    private final UUID checkPointId;
    private final ZonedDateTime createdAt;
    private String title;
    private String content;
    private ZonedDateTime lastUpdateAt;

    private List<Comment> comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckPointItem that = (CheckPointItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CheckPointItem{" +
                "id=" + id +
                ", checkPointId=" + checkPointId +
                ", createdAt=" + createdAt +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", lastUpdateAt=" + lastUpdateAt +
                '}';
    }
}
