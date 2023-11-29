package com.platform.iperform.common.dto;

import com.platform.iperform.model.Question;
import lombok.Builder;

import java.util.List;

@Builder
public class QuestionResponse {
    private final List<Question> questions;
}
