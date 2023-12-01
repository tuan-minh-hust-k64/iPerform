package com.platform.iperform.common.dto;

import com.platform.iperform.model.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class QuestionResponse {
    private final List<Question> questions;
}
