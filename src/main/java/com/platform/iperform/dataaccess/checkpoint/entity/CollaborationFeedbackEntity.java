package com.platform.iperform.dataaccess.checkpoint.entity;

import com.platform.iperform.common.valueobject.FeedbackStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Builder
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "collaboration_feedback")
public class CollaborationFeedbackEntity {
    @Id
    private UUID id;
    private UUID reviewerId;
    private UUID targetId;
    private String strengths;
    private String weaknesses;
    private String timePeriod;
    @Enumerated(EnumType.STRING)
    private FeedbackStatus status;
    private ZonedDateTime createdAt;
    private ZonedDateTime lastUpdateAt;
}
