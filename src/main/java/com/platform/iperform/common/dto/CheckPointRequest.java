package com.platform.iperform.common.dto;

import com.platform.iperform.model.CheckPoint;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class CheckPointRequest {
    private final CheckPoint checkPoint;
    private final UUID userId;
}
