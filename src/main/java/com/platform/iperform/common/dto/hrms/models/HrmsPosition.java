package com.platform.iperform.common.dto.hrms.models;

import lombok.Data;

import java.util.List;

@Data
public class HrmsPosition extends BaseHrmsModel{
    private String name;
    private Boolean is_manager;
    private List<HrmsRoleProject> role_projects;
    private List<HrmsUser> users;
}
