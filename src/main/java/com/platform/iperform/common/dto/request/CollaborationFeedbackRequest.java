package com.platform.iperform.common.dto.request;

import com.platform.iperform.model.CollaborationFeedback;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class CollaborationFeedbackRequest {
    private final UUID id;
    private final UUID reviewerId;
    private final UUID targetId;
    private final String timePeriod;
    private final CollaborationFeedback collaborationFeedback;
    private final List<CollaborationFeedback> collaborationFeedbacks;
}
