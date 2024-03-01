package com.platform.iperform.common.dto.hrms.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HrmsResourceType extends BaseHrmsModel {
    private String name;
    @JsonProperty("api_keys")
    private HrmsApiKey apiKeys;
    private HrmsResource resources;
}
