package com.platform.iperform.common.dto.hrms.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HrmsRolePermission  extends  BaseHrmsModel{
    private HrmsPermission permissions;
    private HrmsResource resources;
    @JsonProperty("role_projects")
    private HrmsRoleProject roleProjects;
}
