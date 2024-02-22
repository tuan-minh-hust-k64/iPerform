package com.platform.iperform.common.dto.hrms.models;

import lombok.Data;

@Data
public class HrmsPermission extends BaseHrmsModel{
    private String name;
    private HrmsRolePermission role_permissions;
}
