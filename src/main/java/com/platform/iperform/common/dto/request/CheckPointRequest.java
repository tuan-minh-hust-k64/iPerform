package com.platform.iperform.common.dto.request;

import com.platform.iperform.model.CheckPoint;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
public class CheckPointRequest {
    private final UUID checkPointId;
    private final CheckPoint checkPoint;
    private final UUID userId;
    private final String receiver;
    private final String title;
}
