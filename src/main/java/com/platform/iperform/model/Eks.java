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
    private UUID userId;
    private final ZonedDateTime createdAt;
    private EksType type;
    private String content;
    private int process;
    private EksStatus status;
    private String timePeriod;
    private ZonedDateTime lastUpdateAt;
    private int ordinalNumber;
    private String description;

    private List<KeyStep> keySteps;
    private List<Comment> comments;
    private final List<CheckIn> checkIns;

//    public void initEks()

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
