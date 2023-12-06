package com.platform.iperform.common.dto.request;

import com.platform.iperform.model.CheckIn;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class CheckInRequest {
    private final UUID id;
    private final List<CheckIn> checkIns;
}
