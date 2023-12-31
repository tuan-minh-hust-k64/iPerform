package com.platform.iperform.model;

import com.platform.iperform.common.valueobject.EksStatus;
import com.platform.iperform.common.valueobject.EksType;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
@Builder
@Getter
@Setter
@AllArgsConstructor
public class Eks {
    private final UUID id;
    private final UUID userId;
    private final ZonedDateTime createdAt;
    private EksType type;
    private String content;
    private int process;
    private EksStatus status;
    private String timePeriod;
    private ZonedDateTime lastUpdateAt;
    private int ordinalNumber;

    private final List<KeyStep> keySteps;
    private final List<Comment> comments;
    private final List<CheckIn> checkIns;

    @Override
    public String toString() {
        return "Eks{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", createdAt=" + createdAt +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", process=" + process +
                ", status=" + status +
                ", timePeriod='" + timePeriod + '\'' +
                ", lastUpdateAt=" + lastUpdateAt +
                ", ordinalNumber=" + ordinalNumber +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Eks eks = (Eks) o;
        return Objects.equals(id, eks.id) && Objects.equals(userId, eks.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId);
    }
}
