package com.platform.iperform.common.dto.hrms.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HrmsResource extends BaseHrmsModel {
    private String name;
    private String description;
    @JsonProperty("resource_types")
    private HrmsResourceType resourceTypes;
    @JsonProperty("role_permissions")
    private HrmsRolePermission rolePermissions;
}
