package com.platform.iperform.common.dto;

import com.platform.iperform.model.KeyStep;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
public class KeyStepRequest {
    private final UUID keyStepId;
    private final List<KeyStep> keySteps;
    private String message;
}
