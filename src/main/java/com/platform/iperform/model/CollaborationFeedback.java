package com.platform.iperform.model;

import com.platform.iperform.common.valueobject.FeedbackStatus;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CollaborationFeedback {
    private final UUID id;
    private final UUID reviewerId;
    private final UUID targetId;
    private String strengths;
    private String weaknesses;
    private String timePeriod;
    private FeedbackStatus status;
    private ZonedDateTime createdAt;
    private ZonedDateTime lastUpdateAt;
}
