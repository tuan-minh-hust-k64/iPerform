package com.platform.iperform.common.dto.hrms.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class HrmsPosition extends BaseHrmsModel{
    private String name;
    @JsonProperty("is_manager")
    private Boolean isManager;
    @JsonProperty("role_projects")
    private List<HrmsRoleProject> roleProjects;
    private List<HrmsUser> users;
}
