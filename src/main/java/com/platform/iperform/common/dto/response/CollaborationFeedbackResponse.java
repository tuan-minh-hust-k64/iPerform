package com.platform.iperform.common.dto.response;

import com.platform.iperform.model.CollaborationFeedback;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CollaborationFeedbackResponse {
    private final List<CollaborationFeedback> collaborationFeedbacks;
    private final CollaborationFeedback data;
}
