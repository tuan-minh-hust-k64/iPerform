package com.platform.iperform.model;

import com.platform.iperform.common.valueobject.QuestionStatus;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class Question {
    private final UUID id;
    private String content;
    private QuestionStatus status;
    private final ZonedDateTime createdAt;
    private ZonedDateTime lastUpdateAt;
}
