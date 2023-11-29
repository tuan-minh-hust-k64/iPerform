package com.platform.iperform.common.dto;

import com.platform.iperform.model.Question;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class QuestionRequest {
    private final List<Question> questions;
}
