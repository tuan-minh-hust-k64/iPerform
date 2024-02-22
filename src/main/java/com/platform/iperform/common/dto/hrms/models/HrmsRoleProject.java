package com.platform.iperform.common.dto.hrms.models;

import lombok.Data;

import java.util.List;

@Data
public class HrmsRoleProject extends BaseHrmsModel{
    private String name;
    private List<HrmsRolePermission> role_permissions;
    private HrmsPosition positions;
    private List<HrmsUser> users;
}
