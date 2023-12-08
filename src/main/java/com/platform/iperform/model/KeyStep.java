package com.platform.iperform.model;

import com.platform.iperform.common.valueobject.EksStatus;
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
public class KeyStep {
    private final UUID id;
    private final UUID eksId;
    private final ZonedDateTime createdAt;
    private String content;
    private EksStatus status;
    private ZonedDateTime lastUpdateAt;
    private int ordinalNumber;

    @Override
    public String toString() {
        return "KeyStep{" +
                "id=" + id +
                ", eId=" + eksId +
                ", createdAt=" + createdAt +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", lastUpdateAt=" + lastUpdateAt +
                ", ordinalNumber=" + ordinalNumber +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyStep keyStep = (KeyStep) o;
        return Objects.equals(id, keyStep.id) && Objects.equals(eksId, keyStep.eksId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eksId);
    }
}
