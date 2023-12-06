package com.platform.iperform.common.dto.response;

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
