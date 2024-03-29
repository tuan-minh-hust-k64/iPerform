package com.platform.iperform.common.dto.response;

import com.platform.iperform.model.CheckPoint;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CheckPointResponse {
    private final List<CheckPoint> checkPoint;
    private final String message;
    private final CheckPoint data;
}
