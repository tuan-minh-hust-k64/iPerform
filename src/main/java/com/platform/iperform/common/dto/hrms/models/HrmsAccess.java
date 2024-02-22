package com.platform.iperform.common.dto.hrms.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HrmsAccess {
    private String resource;
    private String permission;
}
