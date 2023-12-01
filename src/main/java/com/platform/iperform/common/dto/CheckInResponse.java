package com.platform.iperform.common.dto;

import com.platform.iperform.model.CheckIn;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Builder
@Getter
public class CheckInResponse {
    private final List<CheckIn> checkIns;
    private final String message;
}
