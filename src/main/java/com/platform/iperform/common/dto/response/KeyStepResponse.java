package com.platform.iperform.common.dto.response;

import com.platform.iperform.model.KeyStep;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class KeyStepResponse {
    private final List<KeyStep> keySteps;
    private String message;
}
