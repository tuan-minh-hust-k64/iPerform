package com.platform.iperform.common.dto;

import com.platform.iperform.model.Eks;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
@Builder
@AllArgsConstructor
@Getter
public class EksRequest {
    private final UUID userId;
    private final List<Eks> eks;
    private final String timePeriod;
}
