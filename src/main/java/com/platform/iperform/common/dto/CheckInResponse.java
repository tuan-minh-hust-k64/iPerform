package com.platform.iperform.common.dto;

import com.platform.iperform.model.CheckIn;
import lombok.Builder;

import java.util.List;
@Builder
public class CheckInResponse {
    private final List<CheckIn> checkIns;
    private final String message;
}
