package com.platform.iperform.common.dto;

import com.platform.iperform.common.valueobject.QuestionStatus;
import com.platform.iperform.model.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
public class QuestionRequest {
    private final List<Question> questions;
    private String message;
    private QuestionStatus status;
}
