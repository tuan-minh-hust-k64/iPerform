package com.platform.iperform.common.dto.hrms.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HrmsPermission extends BaseHrmsModel{
    private String name;
    @JsonProperty("role_permissions")
    private HrmsRolePermission rolePermissions;
}
